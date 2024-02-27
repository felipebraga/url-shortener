package dev.felipebraga.urlshortener.service;

import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Transactional
    public void save(Url url) {
        urlRepository.save(url);
    }

    public Optional<Url> findById(ShortCode shortCode, User user) {
        Url url = urlRepository.findById(shortCode, user);
        return Optional.ofNullable(url);
    }

    @Cacheable(cacheNames = "url_visited", key = "#shortCode")
    public Optional<Url> findByShortCode(ShortCode shortCode) {
        Url url = urlRepository.findNotExpiredByShortCode(shortCode, LocalDateTime.now());

        return Optional.ofNullable(url);
    }

    public <X extends Throwable> void inactivateOrThrow(ShortCode shortCode, User user, Supplier<? extends X> exceptionIfNotFound) throws X {
        Url url = urlRepository.findById(shortCode, user);

        if (url == null) {
            throw exceptionIfNotFound.get();
        }

        urlRepository.save(url.inactivate());
    }
}
