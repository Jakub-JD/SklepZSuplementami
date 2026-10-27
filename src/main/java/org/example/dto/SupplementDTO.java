package org.example.dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Obiekt transferowy (DTO) przenoszący dane suplementu.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry name lub category będą wartością null.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class SupplementDTO {
    private final @NonNull String name;
    private final @NonNull String category;
    private final double price;
}