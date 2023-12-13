package dev.felipebraga.urlshortener.service;

import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.repository.ShortCodeRepositoryImpl;
import org.springframework.stereotype.Service;
import org.sqids.Sqids;

import java.util.List;

@Service
public class ShortCodeService {

    private final Sqids sqids;
    private final ShortCodeRepositoryImpl repository;

    public ShortCodeService(Sqids sqids, ShortCodeRepositoryImpl repository) {
        this.sqids = sqids;
        this.repository = repository;
    }

    public ShortCode nextShortCode() {
        Long id = repository.nextId();
        return new ShortCode(id, sqids.encode(List.of(id)));
    }

    public ShortCode decode(String uniqueId) {
        List<Long> decoded = sqids.decode(uniqueId);

        if (sqids.encode(decoded).equals(uniqueId)) {
            return new ShortCode(decoded.getFirst(), uniqueId);
        }
        return new ShortCode(null, uniqueId);
    }
}
