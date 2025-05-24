package oridungjeol.duckhang.board.application.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.board.application.dto.RequestDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Sell;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;
import oridungjeol.duckhang.board.infrastructure.repository.BoardJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.SellJpaRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellService {
    private final BoardJpaRepository boardJpaRepository;
    private final SellJpaRepository sellJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BoardDocumentRepository boardDocumentRepository;

    public Long createSell(UUID authorUuid, RequestDto requestDto) {
        BoardEntity boardEntity = BoardEntity.builder()
                .authorUuid(authorUuid)
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .imageUrl(requestDto.getImageUrl())
                .boardType(BoardType.SELL)
                .build();
        boardJpaRepository.save(boardEntity);

        SellEntity sellEntity= SellEntity.builder()
                .boardId(boardEntity.getId())
                .price(requestDto.getPrice())
                .build();
        sellJpaRepository.save(sellEntity);

        BoardDocument document = BoardDocument.builder()
                .id(boardEntity.getId())
                .authorUuid(boardEntity.getAuthorUuid())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .imageUrl(boardEntity.getImageUrl())
                .createdAt(boardEntity.getCreatedAt())
                .boardType(boardEntity.getBoardType())
                .price(sellEntity.getPrice())
                .build();

        log.info(document.toString());
        boardDocumentRepository.save(document);

        return boardEntity.getId();
    }

    public List<TradeListDto> getAllsells() {
        List<BoardEntity> boardEntities = boardJpaRepository.findAllByBoardType(BoardType.SELL);

        return boardEntities.stream()
                .map(boardEntity -> {
                    SellEntity sellEntity = sellJpaRepository.findByBoardId(boardEntity.getId())
                            .orElseThrow(() -> new EntityNotFoundException("sell not found"));

                    User user = userJpaRepository.findByUuid(boardEntity.getAuthorUuid())
                            .orElseThrow(() -> new EntityNotFoundException("User not found"));

                    return TradeListDto.builder()
                            .id(boardEntity.getId())
                            .title(boardEntity.getTitle())
                            .imageUrl(boardEntity.getImageUrl())
                            .price(sellEntity.getPrice())
                            .nickname(user.getNickname())
                            .createdAt(boardEntity.getCreatedAt())
                            .build();
                })
                .toList();
    }

    public TradeDetailDto getsellDetail(Long id) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found for id " + id));

        SellEntity sellEntity = sellJpaRepository.findByBoardId(boardEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("sell not found for board " + boardEntity.getId()));

        User user = userJpaRepository.findById(boardEntity.getAuthorUuid())
                .orElseThrow(() -> new EntityNotFoundException("User not found for UUID " + boardEntity.getAuthorUuid()));

        return TradeDetailDto.builder()
                .id(boardEntity.getId())
                .nickname(user.getNickname())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .imageUrl(boardEntity.getImageUrl())
                .price(sellEntity.getPrice())
                .createdAt(boardEntity.getCreatedAt())
                .build();
    }
    @Transactional
    public Long updatesell(UUID authorUuid, Long id, RequestDto requestDto) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));
        SellEntity sellEntity = sellJpaRepository.findByBoardId(id)
                .orElseThrow(() -> new RuntimeException("sell not found"));

        Board board = boardEntity.toDomain();
        Sell sell = sellEntity.toDomain();

        board.validateAuthor(authorUuid);

        board.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getImageUrl());
        sell.update(requestDto.getPrice());

        boardEntity.updateFromDomain(board);
        sellEntity.updateFromDomain(sell);

        return boardEntity.getId();
    }

    public void deletesell(UUID authorUuid, Long id) {
        BoardEntity boardEntity = boardJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        SellEntity sellEntity = sellJpaRepository.findByBoardId(id)
                .orElseThrow(() -> new RuntimeException("sell not found"));

        Board board = boardEntity.toDomain();
        board.validateAuthor(authorUuid);

        sellJpaRepository.delete(sellEntity);
        boardJpaRepository.deleteById(id);
    }


}