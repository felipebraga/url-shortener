package dev.felipebraga.urlshortener.config;

import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    record ErrorDetail(String pointer, String detail) {
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull final MethodArgumentNotValidException ex,
                                                                  @NonNull final HttpHeaders headers,
                                                                  @NonNull final HttpStatusCode status,
                                                                  @NonNull final WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY,
                "Validation failed.");

        problemDetail.setType(ex.getBody().getType());
        problemDetail.setInstance(ex.getBody().getInstance());

        if (ex.getErrorCount() > 0) {
            List<ErrorDetail> errors = ex.getFieldErrors().stream()
                    .map(f -> new ErrorDetail(f.getField(), f.getDefaultMessage()))
                    .collect(Collectors.toList());

            problemDetail.setProperty("errors", errors);
        }

        return new ResponseEntity<>(problemDetail, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
