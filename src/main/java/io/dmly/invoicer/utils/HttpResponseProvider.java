package io.dmly.invoicer.utils;

import io.dmly.invoicer.response.HttpResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface HttpResponseProvider {
    HttpResponse getHttpResponse(Map<String, Object> data, String message, String reason, HttpStatus status);

    HttpResponse getHttpResponse(String message, String reason, HttpStatus status);

    HttpResponse getHttpResponse(String reason, HttpStatus status);
}
