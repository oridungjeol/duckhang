package oridungjeol.duckhang.user.application.factory;

import org.springframework.stereotype.Component;
import oridungjeol.duckhang.user.domain.User;

@Component
public class UserFactory {

    public User renewUserScope(User user, double scope) {
        return User.builder()
                .uuid(user.getUuid())
                .nickname(user.getNickname())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .scope(scope)
                .build();
    }
}
