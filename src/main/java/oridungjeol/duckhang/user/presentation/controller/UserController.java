package oridungjeol.duckhang.user.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.user.application.usecase.UserUseCase;
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

    @DeleteMapping()
    @PreAuthorize("#userId == authentication.principal.name")
    public void delete(
            @RequestParam String userId
    ) {
        userUseCase.deleteUser(userId);
    }
}
