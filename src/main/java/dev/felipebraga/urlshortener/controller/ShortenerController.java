package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.controller.response.UrlCreatedResponse;
import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import dev.felipebraga.urlshortener.service.ShortCodeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ShortenerController {

    private final ShortCodeService shortCodeService;
    private final UrlRepository urlRepository;

    public ShortenerController(ShortCodeService shortCodeService, UrlRepository urlRepository) {
        this.shortCodeService = shortCodeService;
        this.urlRepository = urlRepository;
    }

    @PostMapping({"/shortener", "/reducto"})
    public ResponseEntity<UrlCreatedResponse> shortener(@RequestBody @Valid UrlRequest urlRequest,
                                                        UriComponentsBuilder uriBuilder) {
        final ShortCode shortCode = shortCodeService.nextShortCode();
        final URI shortenedUri = uriBuilder.path("{shortCode}").buildAndExpand(shortCode).toUri();

        final Url url = Url.builder(shortCode, urlRequest.url())
                .expiresIn(urlRequest.expiresIn())
                .shortenedUrl(shortenedUri.toString())
                .user(null)
                .build();

        urlRepository.save(url);

        return ResponseEntity.created(shortenedUri).body(UrlCreatedResponse.wrap(url));
    }
}
