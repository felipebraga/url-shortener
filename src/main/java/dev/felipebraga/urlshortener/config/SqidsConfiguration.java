package dev.felipebraga.urlshortener.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sqids.Sqids;

@Configuration
public class SqidsConfiguration {

    private final UrlShortenerProperties properties;

    public SqidsConfiguration(UrlShortenerProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Sqids getSqids() {
        return Sqids.builder()
                .minLength(properties.getSqids().getMinLength())
                .alphabet(properties.getSqids().getAlphabet())
                .build();
    }
}
