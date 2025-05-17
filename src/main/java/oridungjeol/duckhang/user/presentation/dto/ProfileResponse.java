package oridungjeol.duckhang.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileResponse {
    private String uuid;
    private String nickname;
    private double scope;
}
