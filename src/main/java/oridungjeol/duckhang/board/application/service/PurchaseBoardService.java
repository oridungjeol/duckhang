package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.board.application.port.in.BoardUseCase;
import oridungjeol.duckhang.board.infrastructure.redis.BoardStreamPublisher;
import oridungjeol.duckhang.board.presentation.dto.RequestDto;
import oridungjeol.duckhang.board.presentation.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.PurchaseRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
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
    private final UserJpaRepository userJpaRepository;

    private final BoardStreamPublisher boardStreamPublisher;

    @Override
    public boolean supportBoardType(BoardType boardType) {
        return BoardType.PURCHASE == boardType;
    }

    @Override
    public Long createBoard(
            UUID authorUuid,
            BoardType boardType,
            RequestDto requestDto
    ) {
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), boardType);
        Board savedBoard = boardRepository.save(board);

        Purchase purchase = new Purchase(savedBoard.getId(), requestDto.getPrice());
        purchaseRepository.save(purchase);

        boardStreamPublisher.publishBoard(savedBoard);

        return savedBoard.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByBoardType(BoardType.PURCHASE);

        return boards.stream()
                .map(board -> {
                    Purchase purchase = purchaseRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return PurchaseDtoMapper.toTradeListDto(board, purchase, user);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TradeDetailDto getDetailBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        Purchase purchase = purchaseRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));
        User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return PurchaseDtoMapper.toTradeDetailDto(board, purchase, user);
    }

    @Override
    public Long updateBoard(Long boardId, UUID authorUuid, RequestDto requestDto) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        board.validateAuthor(authorUuid);

        Purchase purchase = purchaseRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

        board.updateContent(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        purchase.updatePrice(requestDto.getPrice());

        boardRepository.save(board);
        purchaseRepository.save(purchase);

        return board.getId();
    }

    @Override
    public void deleteBoard(UUID authorUuid, Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));
        board.validateAuthor(authorUuid);

        purchaseRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}
