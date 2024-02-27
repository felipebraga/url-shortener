package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.config.UrlShortenerProperties;
import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.model.Activity;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.ActivityRepository;
import dev.felipebraga.urlshortener.service.UrlService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Controller
public class VisitController {

    private final UrlService urlService;
    private final ActivityRepository activityRepo;
    private final UrlShortenerProperties properties;

    public VisitController(UrlService urlService,
                           ActivityRepository activityRepo,
                           UrlShortenerProperties properties) {
        this.urlService = urlService;
        this.activityRepo = activityRepo;
        this.properties = properties;
    }

    @GetMapping("/{shortCode:\\w{4,12}}")
    public ResponseEntity<?> decode(@PathVariable ShortCode shortCode, @RequestHeader HttpHeaders headers) {
        ResponseStatusException notFoundException = new ResponseStatusException(HttpStatus.NOT_FOUND);

        shortCode.isUnknownThenThrows(() -> notFoundException);

        Url url = urlService.findByShortCode(shortCode).orElseThrow(() -> notFoundException);

        activityRepo.save(new Activity(url, headers.toSingleValueMap()));

        return ResponseEntity
                .status(HttpStatus.TEMPORARY_REDIRECT)
                .cacheControl(CacheControl.maxAge(properties.getVisitedCache()))
                .location(URI.create(url.getSourceUrl()))
                .build();
    }
}
