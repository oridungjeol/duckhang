package oridungjeol.duckhang.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.user.application.usecase.UserUseCase;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;
import oridungjeol.duckhang.user.presentation.dto.UpdatePrivacyRequest;
import oridungjeol.duckhang.user.presentation.dto.UpdateProfileRequest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/{userId}")
    public ProfileResponse profile(@PathVariable String userId) {
        return userUseCase.getProfile(userId);
    }

    @PostMapping("/update")
    public void updateProfile(
            @AuthenticationPrincipal CustomPrincipal principal,
            @ModelAttribute UpdateProfileRequest profileRequest
    ) {
        userUseCase.updateProfile(principal.getName(), profileRequest);
    }

    @PatchMapping("/privacy")
    public void updatePrivacy(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody UpdatePrivacyRequest updatePrivacyRequest
    ) {
        userUseCase.updatePrivacy(principal.getName(), updatePrivacyRequest);
    }

    @DeleteMapping()
    public void delete(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestParam String userId
    ) {
        userUseCase.deleteUser(principal.getName(), userId);
    }
}