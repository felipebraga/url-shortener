package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.config.SqidsConfiguration;
import dev.felipebraga.urlshortener.config.UrlShortenerProperties;
import dev.felipebraga.urlshortener.model.ShortCode;
import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.ActivityRepository;
import dev.felipebraga.urlshortener.repository.ShortCodeRepositoryImpl;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import dev.felipebraga.urlshortener.service.ShortCodeService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.sqids.Sqids;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = VisitController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(SqidsConfiguration.class)
class VisitControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UrlShortenerProperties properties;
    @Autowired
    private Sqids sqids;

    @SpyBean
    private ShortCodeService shortCodeService;
    @MockBean
    private ShortCodeRepositoryImpl shortCodeRepository;
    @MockBean
    private UrlRepository urlRepository;
    @MockBean
    private ActivityRepository activityRepo;

    private static Stream<String> validUniqueId() {
        return Stream.of("zeIRYV", "lXiziY", "gJRA2G", "Tx6pab", "jy4fB1");
    }

    private static Stream<String> invalidUniqueId() {
        return Stream.of("sM28ja", "rPbClY", "8nhR0I", "alXtNv", "QbEA48");
    }

    @ParameterizedTest
    @MethodSource("validUniqueId")
    void whenVisitingValidUniqueIdThenRedirect(String uniqueId) throws Exception {
        ShortCode shortCode = new ShortCode(null, uniqueId);
        Url url = Url.builder(shortCode, "http://example.com").build();

        when(urlRepository.findNotExpiredByShortCode(
                any(ShortCode.class), any(LocalDateTime.class))
        ).thenReturn(Optional.of(url));

        mockMvc.perform(get("/{uniqueId}", uniqueId)
                .contentType(MediaType.ALL))
                .andExpect(status().is3xxRedirection());
    }

    @ParameterizedTest
    @MethodSource("validUniqueId")
    void whenVisitingValidUniqueIdAndItDoesntExistsThen404(String uniqueId) throws Exception {
        when(urlRepository.findNotExpiredByShortCode(
                any(ShortCode.class), any(LocalDateTime.class))
        ).thenReturn(Optional.empty());

        mockMvc.perform(get("/{uniqueId}", uniqueId)
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @MethodSource("invalidUniqueId")
    void whenVisitingInvalidUniqueIdThen404(String uniqueId) throws Exception {
        mockMvc.perform(get("/{uniqueId}", uniqueId)
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound());
    }
}