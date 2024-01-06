package io.dmly.invoicer.security.entrypoint;

import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.HttpResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final HttpResponseProvider httpResponseProvider;
    private final HttpResponseWriter httpResponseWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        var httpResponse = httpResponseProvider.getHttpResponse("Login required", httpStatus);
        httpResponseWriter.write(response, httpResponse, httpStatus);
    }
}
