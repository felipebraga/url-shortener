package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.Validation;
import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.controller.response.UrlCreatedResponse;
import dev.felipebraga.urlshortener.controller.response.UrlResponse;
import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import dev.felipebraga.urlshortener.service.ShortCodeService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    private final ShortCodeService shortCodeService;
    private final UrlRepository urlRepository;

    public ShortenerController(ShortCodeService shortCodeService, UrlRepository urlRepository) {
        this.shortCodeService = shortCodeService;
        this.urlRepository = urlRepository;
    }

    @PostMapping(value = "/shorten", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UrlCreatedResponse> shortener(@RequestBody @Validated(Validation.class) UrlRequest urlRequest,
                                                        @AuthenticationPrincipal User user,
                                                        UriComponentsBuilder uriBuilder) {
        return executeShorten(urlRequest, user, uriBuilder);
    }

    @PostMapping(value = "/reducio", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UrlCreatedResponse> reducio(@RequestBody @Valid UrlRequest urlRequest,
                                                      @AuthenticationPrincipal User user,
                                                      UriComponentsBuilder uriBuilder) {
        return executeShorten(urlRequest, user, uriBuilder);
    }

    private ResponseEntity<UrlCreatedResponse> executeShorten(UrlRequest urlRequest,
                                                              User user,
                                                              UriComponentsBuilder uriBuilder) {
        final ShortCode shortCode = shortCodeService.nextShortCode();
        final URI shortenedUri = uriBuilder.path("{shortCode}").buildAndExpand(shortCode).toUri();

        final Url url = Url.builder(shortCode, urlRequest.url())
                .expiresIn(urlRequest.expiresIn())
                .shortenedUrl(shortenedUri)
                .user(user)
                .build();

        urlRepository.save(url);

        return ResponseEntity.created(getResourceLocation(shortCode, user)).body(UrlCreatedResponse.wrap(url));
    }

    @GetMapping({"/shorten/{uniqueId}", "/reducio/{uniqueId}"})
    public ResponseEntity<UrlResponse> getShortened(@PathVariable String uniqueId,
                                                    @AuthenticationPrincipal User user) {
        final ShortCode shortCode = shortCodeService.decode(uniqueId);
        final Url url = urlRepository.findByIdAndUser(shortCode.getSeq(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(UrlResponse.wrap(url));
    }

    @DeleteMapping({"/shorten/{uniqueId}", "/reducto/{uniqueId}"})
    public ResponseEntity<UrlResponse> delete(@PathVariable String uniqueId,
                                              @AuthenticationPrincipal User user) {
        final ShortCode shortCode = shortCodeService.decode(uniqueId);
        final Url url = urlRepository.findByIdAndUser(shortCode.getSeq(), user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        urlRepository.save(url.inactivate());

        return ResponseEntity.noContent().build();
    }

    private URI getResourceLocation(ShortCode shortCode, User user) {
        UriComponentsBuilder currentUri = ServletUriComponentsBuilder.fromCurrentRequest();
        if (user != null && currentUri.toUriString().contains("/reducio")) {
            currentUri.replacePath("/api/shorten");
        }

        return currentUri
                .pathSegment("{shortCode}")
                .buildAndExpand(shortCode).toUri();
    }
}
