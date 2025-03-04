package com.green.acamatch.config.exception;

import com.green.acamatch.entity.myenum.UserRole;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRoleConverter implements Converter<String, UserRole> {
    @Override
    public UserRole convert(String source) {
        return UserRole.fromJson(source);
    }
}
