package dev.felipebraga.urlshortener.service;

import dev.felipebraga.urlshortener.config.UrlShortenerProperties;
import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.repository.ShortCodeRepository;
import org.springframework.stereotype.Component;
import org.sqids.Sqids;

import java.util.List;

@Component
public class ShortCodeComponent {

    private final Sqids sqids;
    private final ShortCodeRepository repository;

    public ShortCodeComponent(ShortCodeRepository repository, UrlShortenerProperties properties) {
        this.sqids = Sqids.builder()
                .minLength(properties.getSqids().getMinLength())
                .alphabet(properties.getSqids().getAlphabet())
                .build();
        this.repository = repository;
    }

    public ShortCode nextShortCode() {
        Long id = repository.nextId();
        return new ShortCode(id, sqids.encode(List.of(id)));
    }

    public ShortCode decode(String encoded) {
        List<Long> decoded = sqids.decode(encoded);

        if (sqids.encode(decoded).equals(encoded)) {
            return new ShortCode(decoded.getFirst(), encoded);
        }
        return new ShortCode(null, encoded);
    }
}
