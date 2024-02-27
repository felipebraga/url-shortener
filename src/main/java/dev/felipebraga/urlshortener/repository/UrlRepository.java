package dev.felipebraga.urlshortener.repository;

import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UrlRepository extends CrudRepository<Url, Long> {

    @Query("select url from Url url where url.shortCode = ?1 and url.user = ?2")
    Url findById(ShortCode shortCode, User user);

    @Query("select url from Url url where url.shortCode = ?1 and (url.expiresIn is null or url.expiresIn >= ?2)")
    Url findNotExpiredByShortCode(ShortCode shortCode, LocalDateTime today);

    void deleteByExpiresInIsLessThan(LocalDateTime now);
}
