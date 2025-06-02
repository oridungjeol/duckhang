package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.board.application.port.out.UploadFilePort;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventDtoMapper;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.infrastructure.redis.infrastructure.BoardStreamPublisher;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeDetailDto;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.PurchaseRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.PurchasePost;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.application.mapper.PurchaseDtoMapper;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseBoardService implements BoardUseCase {

    private final BoardRepository boardRepository;
    private final PurchaseRepository purchaseRepository;
    private final UploadFilePort uploadFilePort;
    private final UserJpaRepository userJpaRepository;

    private final BoardStreamPublisher boardStreamPublisher;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return BoardType.PURCHASE == boardType;
    }

    @Override
    public Long createBoard(UUID authorUuid, BoardType boardType, RequestDto requestDto,
                            MultipartFile imageFile) {

        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadFilePort.upload(imageFile);
        }

        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), imageUrl, boardType);
        Board savedBoard = boardRepository.save(board);

        PurchasePost purchasePost = new PurchasePost(savedBoard.getId(), requestDto.getPrice());
        purchaseRepository.save(purchasePost);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(savedBoard, purchasePost, BoardEventType.CREATE);
        boardStreamPublisher.publishBoard(eventDto);

        return savedBoard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getAllBoards(BoardType boardType) {
        List<Board> boards = boardRepository.findAllByBoardType(boardType);

        return boards.stream()
                .map(board-> {
                    PurchasePost purchasePost = purchaseRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return PurchaseDtoMapper.toTradeListDto(board, purchasePost);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        PurchasePost purchasePost = purchaseRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return PurchaseDtoMapper.toTradeDetailDto(board, purchasePost, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto, MultipartFile imageFile) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        PurchasePost purchasePost = purchaseRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

        String imageUrl = board.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadFilePort.upload(imageFile);
        }

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), imageUrl);
        purchasePost.updatePrice(requestDto.getPrice());

        boardRepository.save(board);
        purchaseRepository.save(purchasePost);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, purchasePost, BoardEventType.UPDATE);
        boardStreamPublisher.publishBoard(eventDto);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        PurchasePost purchasePost = purchaseRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, purchasePost, BoardEventType.DELETE);
        boardStreamPublisher.publishBoard(eventDto);

        purchaseRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
