package oridungjeol.duckhang.review.domain.model;

import lombok.Getter;

@Getter
public final class Content {
    private final String value;

    public Content(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용은 비어 있을 수 없습니다.");
        }
        this.value = value;
    }
}

