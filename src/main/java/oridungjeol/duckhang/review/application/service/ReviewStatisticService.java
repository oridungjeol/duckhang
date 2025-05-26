package oridungjeol.duckhang.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.review.application.dto.ScopeSumDto;
import oridungjeol.duckhang.review.infrastructure.repository.ReviewJpaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewStatisticService {

    private final ReviewJpaRepository reviewJpaRepository;

    public double calculateAverageScope(UUID uuid, double scope) {
        ScopeSumDto scopeSum = reviewJpaRepository.findScopeSumByTargetId(uuid);
        double sum = scopeSum.getSum() + scope;
        double columnCnt = (double) (scopeSum.getColumnCnt() + 1);
        return sum / columnCnt;
    }
}
