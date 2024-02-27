package dev.felipebraga.urlshortener.datatype;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

public final class ShortCode implements Serializable {

    @Serial
    private static final long serialVersionUID = 0L;
    private final Long seq;
    private final String value;

    public ShortCode(Long seq, String value) {
        this.seq = seq;
        this.value = value;
    }

    public Long getSeq() {
        return seq;
    }

    public String getValue() {
        return value;
    }

    /**
     * When the shortCode instance has no seq or value,
     * it is considered an unknown shortCode then it should throw an exception
     */
    public <X extends Throwable> void isUnknownThenThrows(Supplier<? extends X> exceptionSupplier) throws X {
       if (seq == null || value == null) {
           throw exceptionSupplier.get();
       }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShortCode) obj;
        return Objects.equals(this.seq, that.seq) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, value);
    }

    @Override
    public String toString() {
        return value;
    }

}
