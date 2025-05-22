//package oridungjeol.duckhang.board.infrastructure.elasticsearch.document;
//
//import lombok.*;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import oridungjeol.duckhang.board.support.enums.BoardType;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Document(indexName = "board")
//public class BoardDocument {
//
//    @Id
//    private Long id;
//
//    private String authorUuid;
//
//    private String title;
//
//    private String content;
//
//    private String imageUrl;
//
//    private LocalDateTime createdAt;
//
//    private BoardType boardType;
//}
