package io.dmly.invoicer.security.filter;

import io.dmly.invoicer.exception.handler.AuthorizationExceptionHandler;
import io.dmly.invoicer.jwt.provider.TokenProvider;
import io.dmly.invoicer.utils.TokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static io.dmly.invoicer.security.constant.SecurityConstants.PUBLIC_URLS;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final AuthorizationExceptionHandler authorizationExceptionHandler;
    private final TokenExtractor tokenExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(request);
            String email = tokenProvider.getSubject(token, request);

            List<GrantedAuthority> authorities = tokenProvider.getAuthoritiesFromToken(token);
            Authentication authentication = tokenProvider.getAuthentication(email, authorities, request);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("Authentication error", e);
            authorizationExceptionHandler.handle(response, e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (isPublicUrl(request.getRequestURI()) || HttpMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        return tokenExtractor.extractToken(request).isEmpty();
    }

    private boolean isPublicUrl(String requestUri) {
        for (String publicUrl : PUBLIC_URLS) {
            if (requestUri.contains(publicUrl)) {
                return true;
            }
        }
        return false;
    }

    private String getToken(HttpServletRequest request) {
        return tokenExtractor.extractToken(request)
                .orElse(StringUtils.EMPTY);
    }
}
