package org.example.dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class OrderDTO {
    private final @NonNull String orderNumber;
    private final double totalAmount;
}