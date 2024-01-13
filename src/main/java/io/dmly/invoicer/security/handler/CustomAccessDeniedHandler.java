package io.dmly.invoicer.security.handler;

import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.HttpResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final HttpResponseProvider httpResponseProvider;
    private final HttpResponseWriter httpResponseWriter;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        var httpResponse = httpResponseProvider.getHttpResponse("You don't have enough permission", httpStatus);
        httpResponseWriter.write(response, httpResponse, httpStatus);
        log.warn("Access denied: ", accessDeniedException);
    }
}
