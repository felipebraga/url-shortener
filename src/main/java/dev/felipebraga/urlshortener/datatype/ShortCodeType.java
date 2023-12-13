package dev.felipebraga.urlshortener.datatype;

import dev.felipebraga.urlshortener.model.ShortCode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.spi.ValueAccess;
import org.hibernate.usertype.CompositeUserType;

import java.io.Serializable;

public class ShortCodeType implements CompositeUserType<ShortCode> {

    @Override
    public ShortCode instantiate(ValueAccess values, SessionFactoryImplementor sessionFactory) {
        return new ShortCode(values.getValue(0, Long.class), values.getValue(1, String.class));
    }

    @Override
    public Object getPropertyValue(ShortCode component, int property) throws HibernateException {
        return switch (property) {
            case 0 -> component.getId();
            case 1 -> component.getValue();
            default -> throw new IllegalArgumentException(property +
                    " is an invalid property index for class type " + component.getClass().getName());
        };
    }

    @Override
    public Class<ShortCode> embeddable() {
        return ShortCode.class;
    }

    @Override
    public Class<ShortCode> returnedClass() {
        return ShortCode.class;
    }

    @Override
    public boolean equals(ShortCode x, ShortCode y) {
        return x.equals(y);
    }

    @Override
    public int hashCode(ShortCode shortCode) {
        return shortCode.hashCode();
    }

    @Override
    public ShortCode deepCopy(ShortCode value) {
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(ShortCode value) {
        return value;
    }

    @Override
    public ShortCode assemble(Serializable cached, Object owner) {
        return (ShortCode) cached;
    }

    @Override
    public ShortCode replace(ShortCode detached, ShortCode managed, Object owner) {
        return detached;
    }
}
