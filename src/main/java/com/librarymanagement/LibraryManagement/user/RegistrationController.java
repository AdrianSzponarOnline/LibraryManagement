package com.librarymanagement.LibraryManagement.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Konstruktor z wstrzykiwaniem (to jest prawidłowe miejsce na @Autowired)
    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Rejestracja użytkownika
    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest request) {
        // Sprawdź, czy user już istnieje
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already taken.";
        }

        // Hashujemy hasło
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Tworzymy obiekt User z rolą np. "ROLE_USER"
        User newUser = new User(
                request.getEmail(),
                request.getUsername(),
                encodedPassword,
                "ROLE_USER"
        );

        // Zapis w bazie
        userRepository.save(newUser);

        return "User registered successfully!";
    }
}
