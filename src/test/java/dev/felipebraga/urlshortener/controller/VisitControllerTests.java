package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.config.UrlShortenerProperties;
import dev.felipebraga.urlshortener.datatype.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.ActivityRepository;
import dev.felipebraga.urlshortener.repository.ShortCodeRepositoryImpl;
import dev.felipebraga.urlshortener.service.ShortCodeComponent;
import dev.felipebraga.urlshortener.service.UrlService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = VisitController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
class VisitControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UrlShortenerProperties properties;

    @SpyBean
    private ShortCodeComponent shortCodeComponent;
    @MockBean
    private ShortCodeRepositoryImpl shortCodeRepository;
    @MockBean
    private UrlService urlService;
    @MockBean
    private ActivityRepository activityRepo;

    private static Stream<String> validShortCode() {
        return Stream.of("zeIRYV", "lXiziY", "gJRA2G", "Tx6pab", "jy4fB1");
    }

    private static Stream<String> invalidShortCode() {
        return Stream.of("sM28ja", "rPbClY", "8nhR0I", "alXtNv", "QbEA48");
    }

    @ParameterizedTest
    @MethodSource("validShortCode")
    void whenVisitingValidShortCodeThenRedirect(String shortCode) throws Exception {
        Url url = Url.builder(new ShortCode(null, shortCode), "http://example.com").build();

        when(urlService.findByShortCode(any(ShortCode.class)))
                .thenReturn(Optional.of(url));

        mockMvc.perform(get("/{shortCode}", shortCode)
                .contentType(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @ParameterizedTest
    @MethodSource("validShortCode")
    void whenVisitingValidShortCodeAndItDoesntExistsThen404(String shortCode) throws Exception {
        when(urlService.findByShortCode(any(ShortCode.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/{shortCode}", shortCode)
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("invalidShortCode")
    void whenVisitingInvalidShortCodeThen404(String shortCode) throws Exception {
        mockMvc.perform(get("/{shortCode}", shortCode)
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound());
    }
}