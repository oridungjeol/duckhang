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

        double existingSum = 0;
        long existingCount = 0;

        if (scopeSum != null) {
            if (scopeSum.getSum() != null) {
                existingSum = scopeSum.getSum();
            }
            if (scopeSum.getColumnCnt() != null) {
                existingCount = scopeSum.getColumnCnt();
            }
        }

        double sum = existingSum + scope;
        double columnCnt = existingCount + 1;

        return sum / columnCnt;
    }


}
