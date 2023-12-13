package dev.felipebraga.urlshortener.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ShortCodeRepositoryImpl {

    private final EntityManager entityManager;

    public ShortCodeRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Long nextId() {
        Query query = entityManager.createNativeQuery("select nextval('short_code_id_seq')", Long.class);
        return (Long) query.getSingleResult();
    }
}
