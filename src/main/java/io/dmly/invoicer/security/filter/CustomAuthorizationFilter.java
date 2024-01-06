package io.dmly.invoicer.security.filter;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.jwt.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static io.dmly.invoicer.security.costant.SecurityConstants.AUTH_TOKEN_PREFIX;
import static io.dmly.invoicer.security.costant.SecurityConstants.PUBLIC_URLS;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            String email = tokenProvider.getSubject(token, request);

            checkTokenValid(email, token);

            List<GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromToken(token);
            Authentication authentication = tokenProvider.getAuthentication(email, authorities, request);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("Authentication error", e);
            //handle
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (PUBLIC_URLS.contains(request.getRequestURI()) || HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return false;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader == null || !authHeader.startsWith(AUTH_TOKEN_PREFIX);
    }

    private String getToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(headerValue -> StringUtils.isNotEmpty(headerValue) && headerValue.startsWith(AUTH_TOKEN_PREFIX))
                .map(headerValue -> headerValue.replaceFirst(AUTH_TOKEN_PREFIX, ""))
                .orElse(StringUtils.EMPTY);
    }

    private void checkTokenValid(String email, String token) {
        if (!tokenProvider.isTokenValid(email, token)) {
            throw new ApiException("Provided token is invalid");
        }
    }
}
