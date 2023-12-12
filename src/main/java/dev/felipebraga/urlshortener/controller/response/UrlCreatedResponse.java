package dev.felipebraga.urlshortener.controller.response;

import dev.felipebraga.urlshortener.model.Url;

import java.time.LocalDateTime;

public record UrlCreatedResponse(String urlShortened, String originalUrl, LocalDateTime expiresIn) {
    public static UrlCreatedResponse wrap(Url url) {
        return new UrlCreatedResponse(url.getShortenedUrl(), url.getOriginalUrl(), url.getExpiresIn());
    }
}
