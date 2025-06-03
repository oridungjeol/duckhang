package oridungjeol.duckhang.board.domain;

import lombok.Getter;

import javax.naming.NoPermissionException;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Board {

    private Long id;
    private UUID authorUuid;
    private String title;
    private String content;
    private String imageUrl;
    private BoardType boardType;
    private LocalDateTime createdAt;

    public Board(UUID authorUuid, String title, String content, String imageUrl, BoardType boardType) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("제목은 필수입니다.");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("내용은 필수입니다.");
        if (imageUrl == null || imageUrl.isBlank()) throw new IllegalArgumentException("이미지는 필수입니다.");

        this.title = title;
        this.authorUuid = authorUuid;
        this.content = content;
        this.imageUrl = imageUrl;
        this.boardType = boardType;
    }

    public Board(Long id, UUID authorUuid, String title, String content, String imageUrl, BoardType boardType, LocalDateTime createdAt) {
        this.id = id;
        this.authorUuid = authorUuid;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.boardType = boardType;
        this.createdAt = createdAt;
    }

    public void updateContent(String title, String content, String imageUrl) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (imageUrl != null) this.imageUrl = imageUrl;
    }

    public void validateAuthor(UUID requesterUuid) {
        if (!this.getAuthorUuid().equals(requesterUuid)) {
            try {
                throw new NoPermissionException("수정 권한이 없습니다.");
            } catch (NoPermissionException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
