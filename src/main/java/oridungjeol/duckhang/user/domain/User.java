package oridungjeol.duckhang.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class User {
    private final UUID uuid;
    private final String nickname;
    private final String name;
    private final String phoneNumber;
    private final String address;
    private final String email;
    private final double scope;
}
