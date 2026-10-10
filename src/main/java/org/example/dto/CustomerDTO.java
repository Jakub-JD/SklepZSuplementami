package org.example.dto;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Accessors(fluent = true)
@Getter
public class CustomerDTO {
    private final @NonNull String firstName;
    private final @NonNull String lastName;
    private final String email;
}