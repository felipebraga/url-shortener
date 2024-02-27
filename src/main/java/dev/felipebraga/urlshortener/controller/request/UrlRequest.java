package dev.felipebraga.urlshortener.controller.request;

import dev.felipebraga.urlshortener.validation.Validation;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record UrlRequest(@NotBlank @URL String url, @Future(groups = Validation.class) LocalDateTime expiresIn) {
}
