package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.config.UrlShortenerProperties;
import dev.felipebraga.urlshortener.model.Activity;
import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.ActivityRepository;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import dev.felipebraga.urlshortener.service.ShortCodeService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
public class VisitController {

    private final ShortCodeService shortCodeService;
    private final UrlRepository urlRepo;
    private final ActivityRepository activityRepo;
    private final UrlShortenerProperties properties;

    public VisitController(ShortCodeService shortCodeService,
                           UrlRepository urlRepo,
                           ActivityRepository activityRepo,
                           UrlShortenerProperties properties) {
        this.shortCodeService = shortCodeService;
        this.urlRepo = urlRepo;
        this.activityRepo = activityRepo;
        this.properties = properties;
    }

    @GetMapping("/{uniqueId:\\w{4,12}}")
    public ResponseEntity<URI> decode(@PathVariable String uniqueId, @RequestHeader HttpHeaders headers) {
        ResponseStatusException notFoundException = new ResponseStatusException(HttpStatus.NOT_FOUND);

        ShortCode shortCode = shortCodeService.decode(uniqueId)
                .isUnknownThenThrows(() -> notFoundException);

        Url url = urlRepo.findNotExpiredByShortCode(shortCode, LocalDateTime.now())
                .orElseThrow(() -> notFoundException);

        Activity activity = new Activity(url, headers.toSingleValueMap());

        activityRepo.save(activity);

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .location(URI.create(url.getSourceUrl()))
                .cacheControl(CacheControl.maxAge(properties.getVisitedCache()).cachePrivate())
                .build();
    }
}
