package dev.felipebraga.urlshortener.repository;

public interface ShortCodeRepository {

    /**
     * Request for the next sequential id
     *
     * @return the sequential id
     */
    Long nextId();
}
