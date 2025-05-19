package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.application.dto.AllPurchaseDto;
import oridungjeol.duckhang.board.application.dto.PurchaseDetailDto;
import oridungjeol.duckhang.board.application.dto.PurchaseRequestDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
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

    public Long createPurchase(
            UUID authorUuid,
            PurchaseRequestDto purchaseRequestDto
    ) {
        BoardEntity boardEntity = BoardEntity.builder()
                .authorUuid(authorUuid)
                .title(purchaseRequestDto.getTitle())
                .content(purchaseRequestDto.getContent())
                .imageUrl(purchaseRequestDto.getImageUrl())
                .boardType(BoardType.PURCHASE)
                .build();

        boardJpaRepository.save(boardEntity);

        PurchaseEntity purchaseEntity = PurchaseEntity.builder()
                .boardId(boardEntity.getId())
                .price(purchaseRequestDto.getPrice())
                .build();

        purchaseJpaRepository.save(purchaseEntity);

        return boardEntity.getId();
    }

    public List<AllPurchaseDto> getAllPurchases() {
        List<BoardEntity> boardEntities = boardJpaRepository.findAllByBoardType(BoardType.PURCHASE);

        return boardEntities.stream()
                .map(boardEntity -> {
                    PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(boardEntity.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Purchase not found"));

                    User user = userJpaRepository.findById(boardEntity.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return AllPurchaseDto.builder()
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

    public PurchaseDetailDto getPurchaseDetail(Long id) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found for id " + id));

        PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(boardEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Purchase not found for board " + boardEntity.getId()));

        User user = userJpaRepository.findById(boardEntity.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found for UUID " + boardEntity.getAuthorUuid()));

        return PurchaseDetailDto.builder()
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
    public void updatePurchase(UUID authorUuid, Long id, PurchaseRequestDto purchaseRequestDto) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        PurchaseEntity purchaseEntity = purchaseJpaRepository.findByBoardId(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        Board board = boardEntity.toDomain();
        Purchase purchase = purchaseEntity.toDomain();

        board.validateAuthor(authorUuid);

        board.update(purchaseRequestDto.getTitle(), purchaseRequestDto.getContent(), purchaseRequestDto.getImageUrl());
        purchase.update(purchaseRequestDto.getPrice());

        boardEntity.updateFromDomain(board);
        purchaseEntity.updateFromDomain(purchase);
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
