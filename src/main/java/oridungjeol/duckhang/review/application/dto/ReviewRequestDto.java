package oridungjeol.duckhang.review.application.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {
    private String targetId;
    private Double scope;
    private String content;
    private String orderId;
}
