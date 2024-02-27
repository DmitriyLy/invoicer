package io.dmly.invoicer.security.constant;

import java.util.List;

import static io.dmly.invoicer.constants.Constants.USER_IMAGE_PATH;

public class SecurityConstants {
    public static final String AUTH_TOKEN_PREFIX = "Bearer ";
    public static final List<String> PUBLIC_URLS = List.of(
            "/api/v1/user/login",
            "/api/v1/user/verify",
            "/api/v1/user/register",
            "/api/v1/user/reset-password",
            "/api/v1/user/verify-password-reset",
            "/api/v1/user/change-password",
            "/api/v1/user/activate/account",
            "/api/v1/user/refresh/token",
            "/" + USER_IMAGE_PATH
    );

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
}
