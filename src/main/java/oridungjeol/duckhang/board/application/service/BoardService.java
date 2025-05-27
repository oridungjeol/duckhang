package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.BoardResponseDto;
import oridungjeol.duckhang.board.application.mapper.BoardDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.presentation.dto.RequestDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
    private final BoardRepository boardRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return List.of(
                BoardType.EXCHANGE,
                BoardType.DELEGATE,
                BoardType.HELPER,
                BoardType.MATE
        ).contains(boardType);
    }

    @Override
    public Long createBoard(
            UUID authorUuid,
            BoardType boardType,
            RequestDto requestDto
    ) {
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), boardType);
        Board savedBoard = boardRepository.save(board);

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
    public List<BoardListResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByBoardType(BoardType.EXCHANGE);

        return boards.stream()
                .map(board -> {
                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));
                    return BoardDtoMapper.toBoardListDto(board, user);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BoardResponseDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return BoardDtoMapper.toBoardDetailDto(board, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());

        boardRepository.save(board);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        boardRepository.deleteById(id);
    }
}
