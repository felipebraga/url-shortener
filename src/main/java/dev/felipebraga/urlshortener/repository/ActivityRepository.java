package dev.felipebraga.urlshortener.repository;

import dev.felipebraga.urlshortener.model.Activity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends CrudRepository<Activity, Long> {
}
