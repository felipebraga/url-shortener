package dev.felipebraga.urlshortener.model;

import dev.felipebraga.urlshortener.datatype.ShortCodeType;
import jakarta.persistence.*;
import org.hibernate.annotations.CompositeType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"shortCode"}))
public class Url implements Serializable {

    @Id
    private Long id;

    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "id", insertable = false, updatable = false)),
            @AttributeOverride(name = "value", column = @Column(name = "short_code"))}
    )
    @CompositeType(ShortCodeType.class)
    private ShortCode shortCode;
    @Column(columnDefinition = "text")
    private String shortenedUrl;
    @Column(nullable = false, columnDefinition = "text")
    private String originalUrl;
    @Column
    private LocalDateTime expiresIn;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "url")
    private Set<Activity> activities;

    public Url() {
    }

    private Url(Builder builder) {
        this.id = builder.shortCode.getId();
        this.shortCode = builder.shortCode;
        this.originalUrl = builder.originalUrl;
        this.shortenedUrl = builder.shortenedUrl;
        this.expiresIn = builder.expiresIn;
        this.createdAt = builder.createdAt;
        this.user = builder.user;
        this.active = builder.active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShortCode getShortCode() {
        return shortCode;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public void setShortenedUrl(String shortenedUrl) {
        this.shortenedUrl = shortenedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(LocalDateTime expiresIn) {
        this.expiresIn = expiresIn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void deactivate() {
        this.active = Boolean.FALSE;
    }

    public static Builder builder(ShortCode shortCode, String originalUrl) {
        Objects.requireNonNull(shortCode);
        Objects.requireNonNull(originalUrl);
        return new Builder(shortCode, originalUrl);
    }

    public static final class Builder {
        private final ShortCode shortCode;
        private final String originalUrl;
        private String shortenedUrl;
        private LocalDateTime expiresIn;
        public LocalDateTime createdAt;
        private Boolean active;
        private User user;

        private Builder(ShortCode shortCode, String originalUrl) {
            this.shortCode = shortCode;
            this.originalUrl = originalUrl;
        }

        public Builder shortenedUrl(String shortenedUrl) {
            this.shortenedUrl = shortenedUrl;
            return this;
        }

        public Builder expiresIn(LocalDateTime expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }

        public Builder user(User user) {
            if (user != null && user.getId() != null) {
                this.user = user;
            }
            return this;
        }

        public Url build() {
            if (this.user == null) {
                this.expiresIn = LocalDateTime.now().plusHours(6L);
            }
            this.createdAt = LocalDateTime.now();
            this.active = Boolean.TRUE;
            return new Url(this);
        }
    }
}
