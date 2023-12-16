package dev.felipebraga.urlshortener.repository;

import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByIdAndUser(Long id, User user);

    Optional<Url> findByIdAndExpiresInIsGreaterThanEqualAndActiveTrue(Long id, LocalDateTime today);

    Optional<Url> findByShortCodeAndExpiresInIsGreaterThanEqualAndActiveTrue(ShortCode shortCode, LocalDateTime today);

}
