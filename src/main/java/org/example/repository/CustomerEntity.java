package org.example.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
}