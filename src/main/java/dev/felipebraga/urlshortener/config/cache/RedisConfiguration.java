package dev.felipebraga.urlshortener.config.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.felipebraga.urlshortener.model.Url;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
public class RedisConfiguration {

    @Bean
    @Profile("!test")
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {
        RedisCacheConfiguration authUserCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .enableTimeToIdle();

        RedisCacheConfiguration urlVisited = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, Url.class)))
                .entryTtl(UrlCacheTtl.INSTANCE);

        return RedisCacheManager.builder(connectionFactory)
                .enableStatistics()
//                .withCacheConfiguration("auth_user", authUserCacheConfig)
                .withCacheConfiguration("url_visited", urlVisited)
                .build();
    }

}
