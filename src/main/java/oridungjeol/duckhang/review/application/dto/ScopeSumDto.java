package oridungjeol.duckhang.review.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ScopeSumDto {
    private final Double sum;
    private final Long columnCnt;
}
