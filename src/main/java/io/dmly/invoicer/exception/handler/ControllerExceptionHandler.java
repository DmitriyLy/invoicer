package io.dmly.invoicer.exception.handler;

import io.dmly.invoicer.exception.ApiException;
import io.dmly.invoicer.response.HttpResponse;
import io.dmly.invoicer.utils.HttpResponseProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler implements ErrorController {
    private final HttpResponseProvider httpResponseProvider;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        return new ResponseEntity<>(httpResponseProvider.getHttpResponse(exception.getMessage(), httpStatus), httpStatus);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String validationMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        HttpStatus httpStatus = HttpStatus.resolve(statusCode.value());
        return new ResponseEntity<>(httpResponseProvider.getHttpResponse(validationMessage, httpStatus), httpStatus);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> handleApiException(ApiException exception) {
        return new ResponseEntity<>(
                httpResponseProvider.getHttpResponse(exception.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST
        );
    }
}
