package io.dmly.invoicer.exception.handler.impl;

import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.exception.handler.AuthorizationExceptionHandler;
import io.dmly.invoicer.utils.HttpResponseProvider;
import io.dmly.invoicer.utils.HttpResponseWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class AuthorizationExceptionHandlerImpl implements AuthorizationExceptionHandler {
    private final HttpResponseProvider httpResponseProvider;
    private final HttpResponseWriter httpResponseWriter;
    private final List<Class<?>> knownExceptions;

    @Override
    public void handle(HttpServletResponse response, Exception exception) {
        log.error(exception.getMessage(), exception);

        HttpStatus httpStatus;
        HttpResponse httpResponse;

        if (knownExceptions.contains(exception.getClass())) {
            httpStatus = HttpStatus.BAD_REQUEST;
            httpResponse = httpResponseProvider.getHttpResponse(exception.getMessage(), httpStatus);
        } else {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            httpResponse = httpResponseProvider.getHttpResponse("An error occurred. Please try again later", httpStatus);
        }

        httpResponseWriter.write(response, httpResponse, httpStatus);
    }
}
