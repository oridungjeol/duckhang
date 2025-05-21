package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import oridungjeol.duckhang.board.support.enums.BoardType;

import javax.naming.NoPermissionException;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Board {
    /**
     * TODO
     *      - 작성자 검증 글 수정/삭제는 작성자만 가능 ->	validateAuthor(Long loginUserId)
     *      - 내용 유효성 검증	제목, 내용 등이 비어있지 않아야 함	validateContentNotBlank()
     *      - 글 상태 변경	게시글 상태를 변경하는 로직 (예: 삭제, 임시저장)	markAsDeleted()(softdelete)
     */
    private Long id;
    private UUID authorUuid;
    private String title;
    private String content;
    private String imageUrl;
    private BoardType boardType;

    public void update(String title, String content, String imageUrl) {
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
