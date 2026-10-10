package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.CustomerDTO;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Customer {

    private @NonNull String firstName;
    private @NonNull String lastName;
    private String email;

    public CustomerDTO toDTO() {
        return new CustomerDTO(this.firstName, this.lastName, this.email);
    }

    public void restoreFromDTO(CustomerDTO dto) {
        this.firstName = dto.firstName();
        this.lastName = dto.lastName();
        this.email = dto.email();
    }
}