package dev.felipebraga.urlshortener.config;

import dev.felipebraga.urlshortener.service.ShortCodeComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;

@Configuration
public class ConverterConfiguration {

    /**
     * Weird implementation, need investigation why and how
     */
    @Autowired
    public void addShortCodeConverter(GenericConversionService genericConversionService, ShortCodeComponent shortCodeComponent) {
        genericConversionService.addConverter(new ShortCodeConverter(shortCodeComponent));
    }
}
