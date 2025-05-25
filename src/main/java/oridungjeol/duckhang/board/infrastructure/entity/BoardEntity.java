package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Table(name = "board")
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID authorUuid;

    private String title;

    private String content;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    private LocalDateTime createdAt;

}
