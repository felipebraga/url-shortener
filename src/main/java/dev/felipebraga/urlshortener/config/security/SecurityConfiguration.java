package dev.felipebraga.urlshortener.config.security;

import dev.felipebraga.urlshortener.model.User;
import dev.felipebraga.urlshortener.repository.UserRepository;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .securityMatcher("/api/**")
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/api/reducio").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reducio/**").permitAll()
                .requestMatchers("/api/shorten/**", "/api/reducto/**").authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    @Profile("test")
    public InMemoryUserDetailsManager memoryUserDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.inMemory(1L, "flitwick", passwordEncoder.encode("alohomora"));
        return new InMemoryCustomUserDetailsManager(user);
    }

    @Bean
    @Profile("!test")
    public CustomDaoUserDetailsService customUserDetailsService(UserRepository userRepository) {
        return new CustomDaoUserDetailsService(userRepository);
    }

    @Bean
    @Profile("!test")
    public DaoAuthenticationProvider authenticationProvider(CustomDaoUserDetailsService customDaoUserDetailsService,
                                                            PasswordEncoder passwordEncoder,
                                                            CacheManager cacheManager) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(passwordEncoder);
        provider.setUserDetailsService(customDaoUserDetailsService);
        provider.setUserCache(new SpringCacheBasedUserCache(cacheManager.getCache("auth-db-user")));
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
