package io.dmly.invoicer.security.entrypoint;

import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.HttpResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HttpResponseProvider httpResponseProvider;
    private final HttpResponseWriter httpResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        var httpResponse = httpResponseProvider.getHttpResponse(authException.getMessage(), httpStatus);
        httpResponseWriter.write(response, httpResponse, httpStatus);
        log.warn("Authentication exception occurred: ", authException);
    }
}
