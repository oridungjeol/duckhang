package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;
import oridungjeol.duckhang.board.infrastructure.redis.infrastructure.BoardStreamPublisher;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventDtoMapper;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.BoardResponseDto;
import oridungjeol.duckhang.board.application.mapper.BoardDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.presentation.dto.request.RequestDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
    private final BoardRepository boardRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardStreamPublisher boardStreamPublisher;

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

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(savedBoard , BoardEventType.CREATE);
        boardStreamPublisher.publishBoard(eventDto);

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

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board , BoardEventType.CREATE);
        boardStreamPublisher.publishBoard(eventDto);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, BoardEventType.DELETE);
        boardStreamPublisher.publishBoard(eventDto);

        boardRepository.deleteById(id);
    }
}
