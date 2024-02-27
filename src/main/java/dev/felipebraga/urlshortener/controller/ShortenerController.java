package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.controller.response.UrlCreatedResponse;
import dev.felipebraga.urlshortener.controller.response.UrlResponse;
import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import dev.felipebraga.urlshortener.service.ShortCodeComponent;
import dev.felipebraga.urlshortener.service.UrlService;
import dev.felipebraga.urlshortener.validation.Validation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class ShortenerController {

    private final Log logger = LogFactory.getLog(getClass());

    private final UrlService urlService;
    private final ShortCodeComponent shortCodeComponent;

    public ShortenerController(UrlService urlService, ShortCodeComponent shortCodeComponent) {
        this.urlService = urlService;
        this.shortCodeComponent = shortCodeComponent;
    }

    @PostMapping(value = "/shorten", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UrlCreatedResponse> shortener(@RequestBody @Validated(Validation.class) UrlRequest urlRequest,
                                                        @AuthenticationPrincipal User user,
                                                        UriComponentsBuilder uriBuilder) {
        return executeShorten(urlRequest, user, uriBuilder);
    }

    @PostMapping(value = "/reducio", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UrlCreatedResponse> reducio(@RequestBody @Validated(Validation.class) UrlRequest urlRequest,
                                                      @AuthenticationPrincipal User user,
                                                      UriComponentsBuilder uriBuilder) {
        return executeShorten(urlRequest, user, uriBuilder);
    }

    private ResponseEntity<UrlCreatedResponse> executeShorten(UrlRequest urlRequest,
                                                              User user,
                                                              UriComponentsBuilder uriBuilder) {
        final ShortCode shortCode = shortCodeComponent.nextShortCode();
        final URI shortenedUri = uriBuilder.path("{shortCode}").buildAndExpand(shortCode).toUri();

        final Url url = Url.builder(shortCode, urlRequest.url())
                .expiresIn(urlRequest.expiresIn())
                .shortenedUrl(shortenedUri)
                .user(user)
                .build();

        urlService.save(url);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_LOCATION, getResourceLocation(shortCode, user))
                .body(UrlCreatedResponse.wrap(url));
    }

    @GetMapping({"/shorten/{shortCode:\\w{4,12}}", "/reducio/{shortCode:\\w{4,12}}"})
    public ResponseEntity<UrlResponse> getShortened(@PathVariable ShortCode shortCode,
                                                    @AuthenticationPrincipal User user) {
        final Url url = urlService.findById(shortCode, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(UrlResponse.wrap(url));
    }

    @DeleteMapping({"/shorten/{shortCode:\\w{4,12}}", "/reducto/{shortCode:\\w{4,12}}"})
    public ResponseEntity<?> delete(@PathVariable ShortCode shortCode,
                                    @AuthenticationPrincipal User user) {

        urlService.inactivateOrThrow(shortCode, user, () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.noContent().build();
    }

    private String getResourceLocation(ShortCode shortCode, User user) {
        final UriComponentsBuilder currentUri = ServletUriComponentsBuilder.fromCurrentRequest();
        if (user != null && currentUri.toUriString().contains("/reducio")) {
            currentUri.replacePath("/api/shorten");
        }

        return currentUri
                .pathSegment("{shortCode}")
                .buildAndExpand(shortCode).toUriString();
    }
}
