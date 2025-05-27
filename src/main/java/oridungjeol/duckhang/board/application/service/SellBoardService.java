package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventDtoMapper;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;
import oridungjeol.duckhang.board.infrastructure.redis.infrastructure.BoardStreamPublisher;
import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.board.application.mapper.SellDtoMapper;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.presentation.dto.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.SellRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Sell;
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
    private final BoardStreamPublisher boardStreamPublisher;

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

        Sell sell = new Sell(savedBoard.getId(), requestDto.getPrice());
        sellRepository.save(sell);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(savedBoard, sell, BoardEventType.CREATE);
        boardStreamPublisher.publishBoard(eventDto);

        return savedBoard.getId();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByBoardType(BoardType.SELL);

        return boards.stream()
                .map(board-> {
                    Sell sell = sellRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Sell not found"));

                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return SellDtoMapper.toTradeListDto(board, sell, user);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Sell sell = sellRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Sell not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return SellDtoMapper.toTradeDetailDto(board, sell, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        Sell sell = sellRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Sell not found"));

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        sell.updatePrice(requestDto.getPrice());

        boardRepository.save(board);
        sellRepository.save(sell);

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, sell, BoardEventType.UPDATE);
        boardStreamPublisher.publishBoard(eventDto);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        Sell sell = sellRepository.findByBoardId(id)
                .orElseThrow(() -> new EntityNotFoundException("Sell not found"));

        BoardEventDto eventDto = BoardEventDtoMapper.toDto(board, sell, BoardEventType.DELETE);
        boardStreamPublisher.publishBoard(eventDto);

        sellRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
