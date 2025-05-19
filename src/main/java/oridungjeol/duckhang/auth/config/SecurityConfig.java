package oridungjeol.duckhang.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtAuthenticationFilter;
import oridungjeol.duckhang.auth.infrastructure.oauth2.CustomOAuth2UserService;
import oridungjeol.duckhang.auth.infrastructure.oauth2.OAuth2SuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 필요 시
                )
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/auth/**",
                                "/login/**",
                                "/user/profile"
                        ).permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 ->
                                oauth2
//                                .loginPage("/auth/login")
                                        .defaultSuccessUrl("/user/profile")
                                        .userInfoEndpoint(userInfo ->
                                                userInfo
                                                        .userService(customOAuth2UserService)
                                        )
                                        .successHandler(oAuth2SuccessHandler)
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
