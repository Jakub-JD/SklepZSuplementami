package org.example.dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Obiekt transferowy (DTO) przenoszący dane klienta.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry firtName lub lastName będą wartością null.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class CustomerDTO {
    private final @NonNull String firstName;
    private final @NonNull String lastName;
    private final String email;
}