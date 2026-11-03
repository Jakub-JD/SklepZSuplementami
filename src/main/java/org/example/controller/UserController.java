package org.example.controller;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Kontroler REST odpowiedzialny za zarządzanie użytkownikami,
 * w tym proces rejestracji oraz zabezpieczanie danych logowania.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @ obiekt użytkownika przekazany w ciele żądania (JSON)
     * @return komunikat o pomyślnej rejestracji
     * @throws IllegalArgumentException jeśli przesyłane dane są niekompletne
     */
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);

        if(user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        userRepository.save(user);
        return "Użytkownik zarejestrowany pomyślnie. Hasło zostało zhashowane.";
    }
}