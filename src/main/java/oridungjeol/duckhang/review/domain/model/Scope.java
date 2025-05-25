package oridungjeol.duckhang.review.domain.model;

import lombok.Getter;

@Getter
public final class Scope {
    private final Double value;

    public Scope(Double value) {
        if (value == null || value < 0.0 || value > 5.0 || (value * 10) % 5 != 0) {
            throw new IllegalArgumentException("scope는 0.0부터 5.0까지 0.5 단위로 주어져야 합니다.");
        }
        this.value = value;
    }
}
