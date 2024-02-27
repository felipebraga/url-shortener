package dev.felipebraga.urlshortener.config;

import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.service.ShortCodeComponent;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeConverter implements Converter<String, ShortCode> {

    private final ShortCodeComponent shortCodeComponent;

    public ShortCodeConverter(ShortCodeComponent shortCodeComponent) {
        this.shortCodeComponent = shortCodeComponent;
    }

    @Override
    public ShortCode convert(@NonNull final String source) {
        return shortCodeComponent.decode(source);
    }
}
