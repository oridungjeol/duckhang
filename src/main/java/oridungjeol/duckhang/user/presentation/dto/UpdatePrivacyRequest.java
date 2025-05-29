package oridungjeol.duckhang.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdatePrivacyRequest {
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
}
