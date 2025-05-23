package oridungjeol.duckhang.board.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Table(name = "board")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID authorUuid;

    private String title;

    private String content;

    private String imageUrl;

    private OffsetDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Builder
    public BoardEntity(Long id, UUID authorUuid, String title, String content, String imageUrl, OffsetDateTime createdAt, BoardType boardType) {
        this.authorUuid = authorUuid;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = OffsetDateTime.now();
        this.boardType = BoardType.PURCHASE;
    }

    public Board toDomain() {
        return new Board(id, authorUuid, title, content, imageUrl, boardType);
    }

    public void updateFromDomain(Board board) {
        this.authorUuid = board.getAuthorUuid();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.boardType = board.getBoardType();
    }
}
