package dev.felipebraga.urlshortener.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public final class ShortCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;
    private final Long id;
    private final String value;

    public ShortCode(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public <X extends Throwable> ShortCode isUnknownThenThrows(Supplier<? extends X> exceptionSupplier) throws X {
       if (id == null || value == null) {
           throw exceptionSupplier.get();
       }
       return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShortCode) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }

    @Override
    public String toString() {
        return value;
    }

}
