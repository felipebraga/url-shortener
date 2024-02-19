package dev.felipebraga.urlshortener.config.security;

import dev.felipebraga.urlshortener.model.User;
import dev.felipebraga.urlshortener.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomDaoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomDaoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable("auth-db-user")
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username);
    }
}
