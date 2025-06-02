package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.board.application.mapper.RentalDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.RentalRepository;
import oridungjeol.duckhang.board.application.port.out.UploadFilePort;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.domain.RentalPost;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;
import oridungjeol.duckhang.board.infrastructure.redis.infrastructure.BoardStreamPublisher;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventDtoMapper;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.RentalDetailDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalBoardService implements BoardUseCase {
    private final BoardRepository boardRepository;
    private final RentalRepository rentalRepository;
    private final UserJpaRepository userJpaRepository;
    private final UploadFilePort uploadFilePort;
    private final BoardStreamPublisher boardStreamPublisher;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return BoardType.RENTAL == boardType;
    }

    @Override
    public Long createBoard(
            UUID authorUuid,
            BoardType boardType,
            RequestDto requestDto,
            MultipartFile imageFile
    ) {
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadFilePort.upload(imageFile);
        }
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), imageUrl, boardType);
        Board savedBoard = boardRepository.save(board);

        RentalPost rentalPost = new RentalPost(savedBoard.getId(), requestDto.getPrice(), requestDto.getDeposit());
        rentalRepository.save(rentalPost);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(savedBoard, rentalPost, BoardEventType.CREATE);
        boardStreamPublisher.publishBoard(eventDto);

        return savedBoard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BoardListResponseDto> getAllBoards(BoardType boardType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Board> boards = boardRepository.findAllByBoardType(boardType, pageable);

        List<BoardListResponseDto> dtoList = boards.stream()
                .map(board -> {
                    RentalPost rentalPost = rentalRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
                    return RentalDtoMapper.toRentalListDto(board, rentalPost);
                })
                .toList();

        return new PageImpl<>(dtoList, pageable, boards.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public RentalDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        RentalPost rentalPost = rentalRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return RentalDtoMapper.toRentalDetailDto(board, rentalPost, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto,
                            MultipartFile imageFile) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        RentalPost rentalPost = rentalRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        String imageUrl = board.getImageUrl();

        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = uploadFilePort.upload(imageFile);
        }

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), imageUrl);
        rentalPost.updatePriceAndDeposit(requestDto.getPrice(), requestDto.getDeposit());

        boardRepository.save(board);
        rentalRepository.save(rentalPost);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, rentalPost, BoardEventType.UPDATE);
        boardStreamPublisher.publishBoard(eventDto);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        RentalPost rentalPost = rentalRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, rentalPost, BoardEventType.DELETE);
        boardStreamPublisher.publishBoard(eventDto);

        rentalRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
