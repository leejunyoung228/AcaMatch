package com.green.acamatch.config.security;

import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.jwt.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/api/user", "/api/user/log-out",  "/api/chat", "/api/chat/**","/api/review/user")
                                .authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/user/relationship/list/**")
                                .hasAnyRole(UserRole.PARENT.name(), UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.POST, "/api/user/relationship").hasRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/user/relationship").hasRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/api/user/relationship/**").hasRole(UserRole.PARENT.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/review/academy").hasRole(UserRole.ACADEMY.name())
                                .requestMatchers(HttpMethod.GET, "/api/review/my-academy").hasAnyRole(UserRole.ACADEMY.name(), UserRole.TEACHER.name())
                                .requestMatchers("/api/academy").hasRole(UserRole.ACADEMY.name())
                                .anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
