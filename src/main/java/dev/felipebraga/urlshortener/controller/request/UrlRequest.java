package dev.felipebraga.urlshortener.controller.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public record UrlRequest(@NotBlank @URL String url, @Future LocalDateTime expiresIn) {
}
