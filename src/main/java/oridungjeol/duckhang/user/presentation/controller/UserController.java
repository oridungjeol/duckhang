package oridungjeol.duckhang.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.user.application.usecase.UserUseCase;
import oridungjeol.duckhang.user.presentation.dto.UpdatePrivacyRequest;
import oridungjeol.duckhang.user.presentation.dto.UpdateProfileRequest;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;

    @GetMapping("/{userId}")
    public ProfileResponse profile(@PathVariable String userId) {
        return userUseCase.getProfile(userId);
    }

    @PatchMapping()
    @PreAuthorize("#uuid == authentication.principal.name")
    public void updateProfile(
            @AuthenticationPrincipal String userId,
            @RequestBody UpdateProfileRequest profileRequest
            ) {
        userUseCase.updateProfile(userId, profileRequest);
    }

    @PatchMapping("/privacy")
    @PreAuthorize("#uuid == authentication.principal.name")
    public void updatePrivacy(
            @AuthenticationPrincipal String uuid,
            @RequestBody UpdatePrivacyRequest updatePrivacyRequest
    ) {
        userUseCase.updatePrivacy(uuid, updatePrivacyRequest);
    }

    @DeleteMapping()
    @PreAuthorize("#userId == authentication.principal.name")
    public void delete(
            @RequestParam String userId
    ) {
        userUseCase.deleteUser(userId);
    }
}
