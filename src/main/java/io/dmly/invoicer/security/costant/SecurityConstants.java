package io.dmly.invoicer.security.costant;

import java.util.List;

public class SecurityConstants {
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";
    public static final List<String> PUBLIC_URLS = List.of(
            "/api/v1/user/login",
            "/api/v1/user/verify",
            "/api/v1/user/register"
    );
}
