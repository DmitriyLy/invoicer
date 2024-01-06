package io.dmly.invoicer.utils;

import io.dmly.invoicer.response.HttpResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public interface HttpResponseWriter {
    void write(HttpServletResponse response, HttpResponse httpResponse, HttpStatus httpStatus);
}
