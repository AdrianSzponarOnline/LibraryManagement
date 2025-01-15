package com.librarymanagement.LibraryManagement.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            // Tworzymy token uwierzytelniający z podanym username i password
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // Prosimy Spring Security, by uwierzytelnił użytkownika
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Ustawiamy kontekst security
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Wymuszamy utworzenie sesji (jeśli jeszcze nie istnieje)
            HttpSession session = servletRequest.getSession(true);
            HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
            // (lub wstrzyknij przez konstruktor, by nie tworzyć nowego obiektu)

            repo.saveContext(SecurityContextHolder.getContext(), servletRequest, servletResponse);

            // Możemy zwrócić wiadomość o sukcesie, ewentualnie dane użytkownika
            return ResponseEntity.ok("Logged in successfully!");
        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            // Gdy złe dane logowania
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            // Inne błędy
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Pobierz obecną sesję, jeśli istnieje
        HttpSession session = request.getSession(false);

        // Inwalidacja sesji
        if (session != null) {
            session.invalidate();
        }

        // Wyczyszczenie kontekstu security
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully!");
    }
}
