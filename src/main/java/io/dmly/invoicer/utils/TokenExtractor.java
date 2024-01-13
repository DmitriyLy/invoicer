package io.dmly.invoicer.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface TokenExtractor {
    Optional<String> extractToken(HttpServletRequest request);
}
