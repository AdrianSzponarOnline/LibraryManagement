package com.librarymanagement.LibraryManagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Metoda wywoływana przez Spring Security w celu załadowania danych użytkownika
     * na podstawie nazwy użytkownika (username).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Wyszukujemy użytkownika w bazie
        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Tworzymy listę ról/uprawnień (tu zakładamy jedną rolę z pola userEntity.getRole())

        var authorities = Collections.singletonList(new SimpleGrantedAuthority(userEntity.getRole()));

        // Budujemy obiekt org.springframework.security.core.userdetails.User
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}
