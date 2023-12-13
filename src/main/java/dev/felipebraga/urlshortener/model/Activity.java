package dev.felipebraga.urlshortener.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Entity
public class Activity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime visitedIn;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> headers;
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Url url;

    public Activity() {
    }

    public Activity(Url url, Map<String, String> headers) {
        this.headers = headers;
        this.url = url;
        this.visitedIn = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVisitedIn() {
        return visitedIn;
    }

    public void setVisitedIn(LocalDateTime visitedIn) {
        this.visitedIn = visitedIn;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Url getUrl() {
        return url;
    }

    public void setUrl(Url url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity that = (Activity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
