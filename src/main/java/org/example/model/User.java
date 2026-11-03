package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Klasa reprezentująca model użytkownika (służący do logowania i autoryzacji)
 * w domenie systemu.
 *
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry username, passwordHash lub role będą wartością null.
 */

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;
}