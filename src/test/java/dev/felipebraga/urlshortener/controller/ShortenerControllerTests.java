package dev.felipebraga.urlshortener.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.felipebraga.urlshortener.controller.request.UrlRequest;
import dev.felipebraga.urlshortener.repository.UrlRepository;
import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ShortenerController.class)
class ShortenerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UrlRepository urlRepository;

    private static Stream<Arguments> validUrlNoExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
                .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(), null)));
    }

    private static Stream<Arguments> validUrlWithExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
            .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(),
                        LocalDateTime.now().plusHours(index))));
    }

    private static Stream<Arguments> validUrlWithPastExpires() {
        Faker faker = new Faker();
        return LongStream.rangeClosed(1, 5)
            .mapToObj(index -> Arguments.of(new UrlRequest(faker.internet().url(),
                LocalDateTime.now().minusHours(index))));
    }

    private static Stream<String> whenPublicInvalidUrlThen400() {
        return Stream.of(null, " ", "example.", "www.example.com", "http://www.example.com:8080:8081/page");
    }

    @ParameterizedTest
    @MethodSource("validUrlNoExpires")
    void whenPublicValidUrlThenReturn201(UrlRequest urlRequest) throws Exception {
        mockMvc.perform(post("/api/shortener")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(urlRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @ParameterizedTest
    @MethodSource
    void whenPublicInvalidUrlThen400(String url) throws Exception {
        mockMvc.perform(post("/api/reducto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new UrlRequest(url, null))))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("validUrlNoExpires")
    void whenLoggedValidUrlThenReturn201(UrlRequest urlRequest) throws Exception {
        mockMvc.perform(post("/api/shortener")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"));
    }

    @ParameterizedTest
    @MethodSource("validUrlWithExpires")
    void whenLoggedValidUrlWithExpiresThenReturn201(UrlRequest urlRequest) throws Exception {
        mockMvc.perform(post("/api/shortener")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"));
    }

    @ParameterizedTest
    @MethodSource("validUrlWithPastExpires")
    void whenLoggedValidUrlPastExpiresThenReturn400(UrlRequest urlRequest) throws Exception {
        mockMvc.perform(post("/api/shortener")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(urlRequest)))
            .andExpect(status().isBadRequest());
    }
}