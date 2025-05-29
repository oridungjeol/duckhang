package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.application.mapper.SellDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeDetailDto;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.SellRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.SellPost;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SellBoardService implements BoardUseCase {
    private final BoardRepository boardRepository;
    private final SellRepository sellRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return BoardType.SELL == boardType;
    }

    @Override
    public Long createBoard(
            UUID authorUuid,
            BoardType boardType,
            RequestDto requestDto
    ) {
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), boardType);
        Board savedBoard = boardRepository.save(board);

        SellPost sellPost = new SellPost(savedBoard.getId(), requestDto.getPrice());
        sellRepository.save(sellPost);

        BoardDocument document = BoardDocument.builder()
                .id(savedBoard.getId())
                .authorUuid(savedBoard.getAuthorUuid())
                .title(savedBoard.getTitle())
                .content(savedBoard.getContent())
                .imageUrl(savedBoard.getImageUrl())
                .createdAt(savedBoard.getCreatedAt())
                .boardType(savedBoard.getBoardType())
                .build();

        boardDocumentRepository.save(document);

        return savedBoard.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getAllBoards(BoardType boardType) {
        List<Board> boards = boardRepository.findAllByBoardType(boardType);

        return boards.stream()
                .map(board-> {
                    SellPost sellPost = sellRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Sell not found"));

                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return SellDtoMapper.toTradeListDto(board, sellPost);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        SellPost sellPost = sellRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Sell not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return SellDtoMapper.toTradeDetailDto(board, sellPost, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        SellPost sellPost = sellRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Sell not found"));

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        sellPost.updatePrice(requestDto.getPrice());

        boardRepository.save(board);
        sellRepository.save(sellPost);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        sellRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
