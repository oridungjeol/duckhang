package oridungjeol.duckhang.user.presentation.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateProfileRequest {
    private String userId;
    private String nickname;
    private MultipartFile profileImage;
}
