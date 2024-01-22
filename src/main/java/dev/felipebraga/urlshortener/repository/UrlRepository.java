package dev.felipebraga.urlshortener.repository;

import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

    Optional<Url> findByIdAndUser(Long id, User user);

    @Query("select url from Url url where url.shortCode = ?1 and (url.expiresIn is null or url.expiresIn >= ?2)")
    Optional<Url> findNotExpiredByShortCode(ShortCode shortCode, LocalDateTime today);

    void deleteByExpiresInIsLessThan(LocalDateTime now);
}
