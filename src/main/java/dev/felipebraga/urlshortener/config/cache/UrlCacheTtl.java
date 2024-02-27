package dev.felipebraga.urlshortener.config.cache;


import dev.felipebraga.urlshortener.model.Url;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.time.LocalDateTime;

public enum UrlCacheTtl implements RedisCacheWriter.TtlFunction {

    INSTANCE;

    public static final Duration DEFAULT_HOUR_TTL = Duration.ofHours(12);

    @Override
    public Duration getTimeToLive(Object key, Object value) {
        Url url = (Url) value;
        if (url == null || url.getExpiresIn() == null) {
            return DEFAULT_HOUR_TTL;
        }

        Duration duration = Duration.between(LocalDateTime.now(), url.getExpiresIn());

        if (duration.toHours() < DEFAULT_HOUR_TTL.toHours()) {
            return duration;
        }

        return DEFAULT_HOUR_TTL;
    }
}
