package com.green.acamatch.config.security;

import com.green.acamatch.config.GlobalOauth2;
import com.green.acamatch.config.jwt.JwtTokenProvider;
import com.green.acamatch.config.security.oauth.*;
import com.green.acamatch.entity.myenum.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;
    private final Oauth2AuthenticationCheckRedirectUriFilter oauth2AuthenticationCheckRedirectUriFilter;
    private final Oauth2AuthenticationRequestBasedOnCookieRepository repository;
    private final Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler;
    private final Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler;
    private final MyOauth2UserService myOauth2UserService;
    private final GlobalOauth2 globalOauth2;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/ws/**").permitAll()
                                .requestMatchers("/api/review/academy").hasAnyRole("ADMIN", "ACADEMY")
                                .requestMatchers("/api/user", "/api/chat", "/api/chat/**","/api/user/log-out",  "/api/review/user")
                                .authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/user/relationship/list/**")
                                .hasAnyRole(UserRole.PARENT.name(), UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.POST, "/api/user/relationship").hasRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/user/relationship").hasRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/api/user/relationship/**").hasRole(UserRole.PARENT.name())
                                .requestMatchers(HttpMethod.DELETE, "/api/review/academy").hasRole(UserRole.ACADEMY.name())
                                .requestMatchers(HttpMethod.GET, "/api/review/my-academy").hasAnyRole(UserRole.ACADEMY.name())
                                .requestMatchers("/api/academy").hasRole(UserRole.ACADEMY.name())
                                .requestMatchers(HttpMethod.POST, "/api/visitor/track").permitAll()
                                .anyRequest().permitAll()
                )
                .oauth2Login( oauth2 -> oauth2.authorizationEndpoint( auth -> auth.baseUri( globalOauth2.getBaseUri() )
                                .authorizationRequestRepository(repository) )
                        .redirectionEndpoint( redirection -> redirection.baseUri("/*/oauth2/code/*") ) //BE가 사용하는 redirectUri이다. 플랫폼마다 설정을 할 예정
                        .userInfoEndpoint( userInfo -> userInfo.userService(myOauth2UserService) )
                        .successHandler(oauth2AuthenticationSuccessHandler)
                        .failureHandler(oauth2AuthenticationFailureHandler) )
                .addFilterBefore(oauth2AuthenticationCheckRedirectUriFilter, OAuth2AuthorizationRequestRedirectFilter.class)

                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
