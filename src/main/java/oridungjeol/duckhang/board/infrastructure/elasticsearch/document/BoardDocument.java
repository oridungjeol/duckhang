package oridungjeol.duckhang.board.infrastructure.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "board")
public class BoardDocument {
    @Id
    private Long id;

    private UUID authorUuid;

    private String title;

    private String content;

    private String imageUrl;

    @Field(type = FieldType.Date)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private OffsetDateTime createdAt;

    private int price;

    private BoardType boardType;

}
