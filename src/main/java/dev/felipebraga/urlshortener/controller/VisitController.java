package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@RestController
public class VisitController {

    private final UrlRepository urlRepo;

    public VisitController(UrlRepository urlRepo) {
        this.urlRepo = urlRepo;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<URI> decode(@PathVariable String shortCode) {
        Url url = urlRepo.findByShortCodeAndExpiresInIsGreaterThanEqualAndActiveTrue(shortCode, LocalDateTime.now())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(url.getOriginalUrl()))
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS).cachePrivate())
                .build();
    }
}
