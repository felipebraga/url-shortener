package dev.felipebraga.urlshortener.model;

import dev.felipebraga.urlshortener.datatype.ShortCodeType;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CompositeType;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"short_code"}))
public class Url implements Persistable<Long>, Serializable {

    @Serial
    private static final long serialVersionUID = 0L;

    @Id
    private Long id;

    @NaturalId
    @AttributeOverrides({
        @AttributeOverride(name = "seq", column = @Column(name = "id", insertable = false, updatable = false)),
        @AttributeOverride(name = "value", column = @Column(name = "short_code", length = 12, nullable = false, updatable = false))}
    )
    @CompositeType(ShortCodeType.class)
    private ShortCode shortCode;

    @Column(columnDefinition = "text")
    private String shortenedUrl;

    @Column(nullable = false, columnDefinition = "text")
    private String sourceUrl;

    @Column
    private LocalDateTime expiresIn;

    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault(value = "now()")
    private LocalDateTime createdAt;

    @Column(nullable = false, columnDefinition = "boolean default true", insertable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "url")
    private Set<Activity> activities;

    @Transient
    private boolean isNew = true;

    public Url() {
    }

    private Url(Builder builder) {
        this.id = builder.shortCode.getSeq();
        this.shortCode = builder.shortCode;
        this.sourceUrl = builder.sourceUrl;
        this.shortenedUrl = builder.shortenedUrl;
        this.expiresIn = builder.expiresIn;
        this.user = builder.user;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String originalUrl) {
        this.sourceUrl = originalUrl;
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

    @Override
    public boolean isNew() {
        return isNew;
    }

    public Url inactivate() {
        this.active = Boolean.FALSE;
        return this;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Url url = (Url) o;
        return Objects.equals(id, url.id) && Objects.equals(shortCode, url.shortCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shortCode);
    }

    public static Builder builder(ShortCode shortCode, String sourceUrl) {
        Objects.requireNonNull(shortCode);
        Objects.requireNonNull(sourceUrl);
        return new Builder(shortCode, sourceUrl);
    }

    public static final class Builder {
        private final ShortCode shortCode;
        private final String sourceUrl;
        private String shortenedUrl;
        private LocalDateTime expiresIn;
        private User user;

        private Builder(ShortCode shortCode, String sourceUrl) {
            this.shortCode = Objects.requireNonNull(shortCode);
            this.sourceUrl = Objects.requireNonNull(sourceUrl);
        }

        public Builder shortenedUrl(URI shortenedUrl) {
            Objects.requireNonNull(shortenedUrl);
            this.shortenedUrl = shortenedUrl.toString();
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
            return new Url(this);
        }
    }
}
