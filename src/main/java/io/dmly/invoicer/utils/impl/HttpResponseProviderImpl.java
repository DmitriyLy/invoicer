package io.dmly.invoicer.utils.impl;

import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.utils.HttpResponseProvider;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Component
public class HttpResponseProviderImpl implements HttpResponseProvider {
    @Override
    public HttpResponse getHttpResponse(Map<String, Object> data, String message, String reason, HttpStatus status) {

        var response = HttpResponse.builder()
                .timestamp(LocalDateTime.now().toString())
                .status(status)
                .statusCode(status.value())
                .build();

        if (StringUtils.isNotEmpty(message)) {
            response.setMessage(message);
        }

        if (StringUtils.isNotEmpty(reason)) {
            response.setReason(reason);
        }

        if (MapUtils.isNotEmpty(data)) {
            response.setData(data);
        }

        return response;
    }

    @Override
    public HttpResponse getHttpResponse(String message, String reason, HttpStatus status) {
        return getHttpResponse(Collections.emptyMap(), message, reason, status);
    }

    @Override
    public HttpResponse getHttpResponse(String reason, HttpStatus status) {
        return getHttpResponse(Collections.emptyMap(), StringUtils.EMPTY, reason, status);
    }
}
