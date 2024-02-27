package dev.felipebraga.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.repository.ShortCodeRepository;
import dev.felipebraga.urlshortener.service.ShortCodeComponent;
import dev.felipebraga.urlshortener.service.UrlService;
import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ShortenerController.class,
    excludeAutoConfiguration = SecurityAutoConfiguration.class)
class ShortenerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @SpyBean
    private ShortCodeComponent shortCodeComponent;
    @MockBean
    private UrlService urlService;
    @MockBean
    private ShortCodeRepository shortCodeRepository;

    private static Stream<Arguments> validUrlNoExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
                .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(), null), index));
    }

    private static Stream<Arguments> validUrlWithExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
            .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(),
                        LocalDateTime.now().plusHours(index)), index));
    }

    private static Stream<Arguments> validUrlWithPastExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
            .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(),
                LocalDateTime.now().minusHours(index)), index));
    }

    private static Stream<String> whenPublicInvalidUrlThen422() {
        return Stream.of(null, " ", "example.", "www.example.com", "http://www.example.com:8080:8081/page");
    }

    @ParameterizedTest
    @MethodSource("validUrlNoExpires")
    void whenPublicValidUrlThenReturn201(UrlRequest urlRequest, Long index) throws Exception {
        when(shortCodeRepository.nextId()).thenReturn(index);

        mockMvc.perform(post("/api/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(urlRequest)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource
    void whenPublicInvalidUrlThen422(String url) throws Exception {
        mockMvc.perform(post("/api/reducio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new UrlRequest(url, null))))
                .andExpect(status().isUnprocessableEntity());
    }

    @WithMockUser(username = "flitwick", password = "alohomora")
    @ParameterizedTest
    @MethodSource("validUrlNoExpires")
    void whenLoggedValidUrlThenReturn201(UrlRequest urlRequest, Long index) throws Exception {
        mockMvc.perform(post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("content-location"));
    }

    @WithMockUser(username = "flitwick", password = "alohomora")
    @ParameterizedTest
    @MethodSource("validUrlWithExpires")
    void whenLoggedValidUrlWithExpiresThenReturn201(UrlRequest urlRequest, Long index) throws Exception {
        mockMvc.perform(post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("content-location"));
    }

    @WithMockUser(username = "flitwick", password = "alohomora")
    @ParameterizedTest
    @MethodSource("validUrlWithPastExpires")
    void whenLoggedValidUrlPastExpiresThenReturn422(UrlRequest urlRequest, Long index) throws Exception {
        mockMvc.perform(post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isUnprocessableEntity());
    }
}