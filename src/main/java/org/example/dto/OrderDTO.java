package org.example.dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Obiekt transferowy (DTO) przenoszący dane zamówienia.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametr orederNumber będzie wartością null.
 */
@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class OrderDTO {
    private final @NonNull String orderNumber;
    private final double totalAmount;
}