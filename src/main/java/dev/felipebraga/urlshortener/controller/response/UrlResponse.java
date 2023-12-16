package dev.felipebraga.urlshortener.controller.response;

import dev.felipebraga.urlshortener.model.Url;

import java.time.LocalDateTime;

public record UrlResponse(String shortCode, String shortenedUrl, String sourceUrl, LocalDateTime expiresIn,
                          LocalDateTime createdAt) {
    public static UrlResponse wrap(Url url) {
        return new UrlResponse(url.getShortCode().toString(), url.getShortenedUrl(), url.getSourceUrl(), url.getExpiresIn(), url.getCreatedAt());
    }
}
