package oridungjeol.duckhang.user.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class User {
    private UUID uuid;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private double scope;
}
