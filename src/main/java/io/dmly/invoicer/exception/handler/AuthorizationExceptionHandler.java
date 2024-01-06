package io.dmly.invoicer.exception.handler;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthorizationExceptionHandler {
    void handle(HttpServletResponse response, Exception exception);
}
