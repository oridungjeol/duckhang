package oridungjeol.duckhang.user.support;

import oridungjeol.duckhang.user.domain.User;

public class UserConverter {

    public static oridungjeol.duckhang.user.infrastructure.entity.User toEntity(User user) {
        return oridungjeol.duckhang.user.infrastructure.entity.User.builder()
                .uuid(user.getUuid())
                .nickname(user.getNickname())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .scope(user.getScope())
                .build();
    }

    public static User toDomain(oridungjeol.duckhang.user.infrastructure.entity.User user) {
        return User.builder()
                .uuid(user.getUuid())
                .nickname(user.getNickname())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .email(user.getEmail())
                .scope(user.getScope())
                .build();
    }
}
