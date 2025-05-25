package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.application.dto.RequestDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.application.port.in.PurchaseBoardUseCase;
import oridungjeol.duckhang.board.application.port.out.BoardRepository;
import oridungjeol.duckhang.board.application.port.out.PurchaseRepository;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;
import oridungjeol.duckhang.board.support.mapper.DtoMapper;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseBoardService implements PurchaseBoardUseCase {
    private final BoardRepository boardRepository;
    private final PurchaseRepository purchaseRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    @Override
    public Long createBoard(
            UUID authorUuid,
            RequestDto requestDto
    ) {
        Board board = new Board(authorUuid, requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl(), BoardType.PURCHASE);
        Board savedBoard = boardRepository.save(board);

        Purchase purchase = new Purchase(savedBoard.getId(), requestDto.getPrice());
        purchaseRepository.save(purchase);

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
    public List<TradeListDto> getAllBoards() {
        List<Board> boards = boardRepository.findAllByBoardType(BoardType.PURCHASE);

        return boards.stream()
                .map(board-> {
                    Purchase purchase = purchaseRepository.findByBoardId(board.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

                    User user = userJpaRepository.findByUuid(board.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return DtoMapper.toTradeListDto(board, purchase, user);
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

        return DtoMapper.toTradeDetailDto(board, purchase, user);
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
