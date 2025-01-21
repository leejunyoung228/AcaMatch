package com.green.studybridge.config.jwt;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.studybridge.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class JwtUser{
    private long signedUserId;
    private List<String> roles;

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public JwtUser(User user) {
        signedUserId = user.getUserId();
        roles = new ArrayList<>();
        roles.add(user.getRole().getRoleName());
    }
}
