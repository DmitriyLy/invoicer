package io.dmly.invoicer.jwt.provider;

import io.dmly.invoicer.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface TokenProvider {
    String getAccessToken(User user);

    String getRefreshToken(User user);

    <T extends GrantedAuthority> List<T> getAuthoritiesFromToken(String token);

    Authentication getAuthentication(String email, List<GrantedAuthority> authorities, HttpServletRequest request);

    boolean isTokenValid(String email, String token);

    String getSubject(String token, HttpServletRequest request);
}
