package com.green.acamatch.config;

import com.green.acamatch.config.exception.CommonErrorCode;
import com.green.acamatch.config.exception.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {
    public Cookie getCookie(HttpServletRequest request, String name) {
        return Arrays.stream(Optional.ofNullable(request.getCookies())
                        .orElseThrow(() -> new CustomException(CommonErrorCode.COOKIE_NOT_FOUND)))
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new CustomException(CommonErrorCode.COOKIE_NOT_FOUND));
    }
    public <T> T getValue(HttpServletRequest req, String name, Class<T> valueType) {
        Cookie cookie = getCookie(req, name);
        if (cookie == null) { return null; }
        if(valueType == String.class) {
            return (T) cookie.getValue();
        }
        return deserializeCookie(cookie, valueType);
    }

    //직렬화, 객체가 가지고 있는 값을 문자열로 변환
    private String serializeObject(Object obj) {
        return Base64.getUrlEncoder().encodeToString( SerializationUtils.serialize(obj) );
    }

    //역직렬화, 문자열값을 객체로 변환
    private <T> T deserializeCookie(Cookie cookie, Class<T> valueType) {
        return valueType.cast(
                SerializationUtils.deserialize( Base64.getUrlDecoder().decode(cookie.getValue()) )
        );
    }


    //Res header에 내가 원하는 쿠키를 담는 메소드
    public void setCookie(HttpServletResponse res, String name, String value, int maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath(path); //이 요청으로 들어올 때만 쿠키값이 넘어올 수 있도록
        cookie.setHttpOnly(true); //보안 쿠키 설정, 프론트에서 JS로 쿠키값을 얻을 수 없다.
        cookie.setMaxAge(maxAge);
        res.addCookie(cookie);
    }

    public void setCookie(HttpServletResponse res, String name, Object value, int maxAge, String path) {
        this.setCookie(res, name, serializeObject(value), maxAge, path);
    }

    public void deleteCookie(HttpServletResponse res, String name) {
        setCookie(res, name, null, 0, "");
    }

    public void setCookieToken(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/api/user/access-token");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public void deleteCookieToken(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/api/user/access-token");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
