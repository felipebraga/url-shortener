package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.controller.response.UrlCreatedResponse;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.UrlRepository;

import jakarta.validation.Valid;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ShortenerController {

    private final UrlRepository urlRepository;

    public ShortenerController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping({"/shortener", "/reducto"})
    public ResponseEntity<UrlCreatedResponse> shortener(@RequestBody @Valid UrlRequest urlRequest,
                                                        UriComponentsBuilder uriBuilder) {

        String shortCode = RandomStringUtils.randomAlphanumeric(6);
        URI shortenedUri = uriBuilder.path("{shortCode}").buildAndExpand(shortCode).toUri();

        Url url = new Url();
        url.setShortCode(shortCode);
        url.setOriginalUrl(urlRequest.url());
        url.setCreatedAt(LocalDateTime.now());
        url.setShortenedUrl(shortenedUri.toString());
        url.setActive(Boolean.TRUE);

        urlRepository.save(url);

        return ResponseEntity.created(shortenedUri).body(UrlCreatedResponse.wrap(url));
    }
}
