package dev.felipebraga.urlshortener.config;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Validated
@ConfigurationProperties("url.shortener")
public class UrlShortenerProperties {

    @NestedConfigurationProperty
    private SqidsProperties sqids = new SqidsProperties();

    @DurationMin(seconds = 1L)
    @DurationUnit(ChronoUnit.SECONDS)
    private Duration visitedCache = Duration.ofSeconds(30);

    public SqidsProperties getSqids() {
        return sqids;
    }

    public void setSqids(SqidsProperties sqids) {
        this.sqids = sqids;
    }

    public Duration getVisitedCache() {
        return visitedCache;
    }

    public void setVisitedCache(Duration visitedCache) {
        this.visitedCache = visitedCache;
    }

    @Validated
    public static class SqidsProperties {

        @Size(min = 4, max = 62)
        @Pattern(regexp = "^[a-zA-Z0-9]{4,62}$")
        private String alphabet = null;

        @Min(4)
        @Max(12)
        private int minLength = 4;

        public String getAlphabet() {
            return alphabet;
        }

        public void setAlphabet(String alphabet) {
            this.alphabet = alphabet;
        }

        public int getMinLength() {
            return minLength;
        }

        public void setMinLength(int minLength) {
            this.minLength = minLength;
        }
    }

}
