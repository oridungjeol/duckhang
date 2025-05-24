package oridungjeol.duckhang.auth.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.auth.application.usecase.AuthUseCase;
import oridungjeol.duckhang.auth.presentation.dto.AccessTokenRequestDto;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @GetMapping("/token/refresh")
    public ResponseEntity<String> getAccessToken(@RequestBody AccessTokenRequestDto request) {

        return ResponseEntity.ok(authUseCase.getAccessToken(request));
    }
}
