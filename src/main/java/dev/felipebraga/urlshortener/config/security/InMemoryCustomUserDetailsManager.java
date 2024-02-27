package dev.felipebraga.urlshortener.config.security;

import dev.felipebraga.urlshortener.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCustomUserDetailsManager extends InMemoryUserDetailsManager {

    private final Map<String, User> customUsers = new HashMap<>();

    public InMemoryCustomUserDetailsManager() {
    }

    public InMemoryCustomUserDetailsManager(UserDetails... users) {
        for (UserDetails user : users) {
            createUser(user);
        }
    }

    @Override
    public void createUser(UserDetails user) {
        super.createUser(user);
        this.customUsers.put(user.getUsername().toLowerCase(), (User) user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        super.loadUserByUsername(username);
        User user = this.customUsers.get(username.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return User.inMemory(user.getId(), user.getUsername(), user.getPassword());
    }
}
