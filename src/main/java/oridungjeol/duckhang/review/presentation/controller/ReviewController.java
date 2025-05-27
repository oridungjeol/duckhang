package oridungjeol.duckhang.review.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.review.application.dto.ReviewRequestDto;
import oridungjeol.duckhang.review.application.dto.ReviewResponseDto;
import oridungjeol.duckhang.review.application.usecase.ReviewUseCase;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewUseCase reviewUseCase;

    @PostMapping()
    public ResponseEntity<String> addReview(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody ReviewRequestDto request
    ) {
        return ResponseEntity.ok(reviewUseCase.addReview(principal.getUuid(), request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviews(
            @PathVariable String userId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        return ResponseEntity.ok(reviewUseCase.getReviews(userId, pageNumber, pageSize));
    }
}

