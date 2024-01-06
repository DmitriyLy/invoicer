package io.dmly.invoicer.utils.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.utils.HttpResponseWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class HttpResponseWriterImpl implements HttpResponseWriter {
    @Override
    public void write(HttpServletResponse response, HttpResponse httpResponse, HttpStatus httpStatus) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        try {
            var outputStream = response.getOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(outputStream, httpResponse);
            outputStream.flush();
        } catch (IOException e) {
            log.error("Response writing error", e);
        }
    }
}
