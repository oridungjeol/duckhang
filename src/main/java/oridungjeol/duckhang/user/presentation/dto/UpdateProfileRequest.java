package oridungjeol.duckhang.user.presentation.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UpdateProfileRequest {
    private String userId;
    private String nickname;
    private MultipartFile profileImage;
}
