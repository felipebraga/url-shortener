package dev.felipebraga.urlshortener.controller;

import dev.felipebraga.urlshortener.model.Url;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = VisitController.class)
class VisitControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlRepository urlRepository;

    private static Stream<Arguments> validShortCode() {
        return Stream.of(Arguments.of("WeQdbx", "https://shyann.com"),
            Arguments.of("c0LQw3", "https://lorenzo.net"),
            Arguments.of("3M65TM", "https://ardella.com"),
            Arguments.of("4ahSsr", "https://annamarie.net"),
            Arguments.of("SkURET", "http://uriel.biz"));
    }

    private static Stream<String> notExistsShortCode() {
        return Stream.of("sM28ja", "rPbClY", "8nhR0I", "alXtNv", "QbEA48");
    }

    @ParameterizedTest
    @MethodSource("validShortCode")
    void whenVisitingValidShortCodeThenRedirect(String shortCode, String originalUrl) throws Exception {
        Url url = new Url();
        url.setShortCode(shortCode);
        url.setOriginalUrl(originalUrl);

        when(urlRepository.findByShortCodeAndExpiresInIsGreaterThanEqualAndActiveTrue(
                eq(shortCode), any(LocalDateTime.class))
        ).thenReturn(Optional.of(url));

        mockMvc.perform(get("/{shortCode}", shortCode)
                .contentType(MediaType.ALL))
                .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(originalUrl));
    }

    @ParameterizedTest
    @MethodSource("notExistsShortCode")
    void whenVisitingValidShortCodeAndItDoesntExistsThen404(String shortCode) throws Exception {
        when(urlRepository.findByShortCodeAndExpiresInIsGreaterThanEqualAndActiveTrue(
                eq(shortCode), any(LocalDateTime.class))
        ).thenReturn(Optional.empty());

        mockMvc.perform(get("/{uniqueId}", shortCode)
                        .contentType(MediaType.ALL))
                .andExpect(status().isNotFound());
    }
}