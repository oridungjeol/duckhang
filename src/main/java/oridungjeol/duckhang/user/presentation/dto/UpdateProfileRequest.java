package oridungjeol.duckhang.user.presentation.dto;

import lombok.Getter;

@Getter
public class UpdateProfileRequest {
    private String nickname;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private double scope;
}
