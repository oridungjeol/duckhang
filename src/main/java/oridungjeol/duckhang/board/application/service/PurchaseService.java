package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.RequestDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;
import oridungjeol.duckhang.board.infrastructure.repository.BoardJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.PurchaseJpaRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final BoardJpaRepository boardJpaRepository;
    private final PurchaseJpaRepository purchaseJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    public Long createPurchase(UUID authorUuid, RequestDto requestDto) {
        BoardEntity boardEntity = BoardEntity.builder()
                .authorUuid(authorUuid)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .boardType(BoardType.PURCHASE)
                .build();
        boardJpaRepository.save(boardEntity);

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .boardId(boardEntity.getId())
                .price(requestDto.getPrice())
                .build();
        purchaseJpaRepository.save(purchaseEntity);

        BoardDocument document = BoardDocument.builder()
                .id(boardEntity.getId())
                .authorUuid(boardEntity.getAuthorUuid())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .imageUrl(boardEntity.getImageUrl())
//                .createdAt(boardEntity.getCreatedAt())
                .boardType(boardEntity.getBoardType())
                .price(purchaseEntity.getPrice())
                .build();

        boardDocumentRepository.save(document);

        return boardEntity.getId();
    }

    public List<TradeListDto> getAllPurchases() {
        List<BoardEntity> boardEntities = boardJpaRepository.findAllByBoardType(BoardType.PURCHASE);

        return boardEntities.stream()
                .map(boardEntity -> {
                    PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(boardEntity.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

                    User user = userJpaRepository.findByUuid(boardEntity.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return TradeListDto.builder()
                            .id(boardEntity.getId())
                            .title(boardEntity.getTitle())
                            .imageUrl(boardEntity.getImageUrl())
                            .price(purchaseEntity.getPrice())
                            .nickname(user.getNickname())
                            .createdAt(boardEntity.getCreatedAt())
                            .build();
                })
                .toList();
    }

    public TradeDetailDto getPurchaseDetail(Long id) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found for id " + id));

        PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(boardEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found for board " + boardEntity.getId()));

        User user = userJpaRepository.findById(boardEntity.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found for UUID " + boardEntity.getAuthorUuid()));

        return TradeDetailDto.builder()
                .id(boardEntity.getId())
                .nickname(user.getNickname())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .imageUrl(boardEntity.getImageUrl())
                .price(purchaseEntity.getPrice())
                .createdAt(boardEntity.getCreatedAt())
                .build();
    }
    @Transactional
    public Long updatePurchase(UUID authorUuid, Long id, RequestDto requestDto) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Board board = boardEntity.toDomain();
        Purchase purchase = purchaseEntity.toDomain();

        board.validateAuthor(authorUuid);

        board.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        purchase.update(requestDto.getPrice());

        boardEntity.updateFromDomain(board);
        purchaseEntity.updateFromDomain(purchase);

        return boardEntity.getId();
    }

    public void deletePurchase(UUID authorUuid, Long id) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Board board = boardEntity.toDomain();
        board.validateAuthor(authorUuid);

        purchaseJpaRepository.delete(purchaseEntity);
        boardJpaRepository.deleteById(id);
    }


}
