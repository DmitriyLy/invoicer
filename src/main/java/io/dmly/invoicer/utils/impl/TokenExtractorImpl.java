package io.dmly.invoicer.utils.impl;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.utils.TokenExtractor;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.dmly.invoicer.security.constant.SecurityConstants.AUTH_TOKEN_PREFIX;

@Component
public class TokenExtractorImpl implements TokenExtractor {
    @Override
    public Optional<String> extractToken(HttpServletRequest request) {
        if (request == null) {
            throw new ApiException("Request is null");
        }
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(headerValue -> StringUtils.isNotEmpty(headerValue) && headerValue.startsWith(AUTH_TOKEN_PREFIX))
                .map(headerValue -> headerValue.replaceFirst(AUTH_TOKEN_PREFIX, ""));
    }
}
