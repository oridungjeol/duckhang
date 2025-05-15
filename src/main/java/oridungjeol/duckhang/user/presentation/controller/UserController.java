package oridungjeol.duckhang.user.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/{userId}")
    public ProfileResponse profile(@PathVariable String userId) {
        return null;
    }
}
