package oridungjeol.duckhang.user.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private UUID uuid;
    @Column(unique = true, nullable = false)
    private String nickname;
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    @Column(nullable = false)
    private double scope;
}
