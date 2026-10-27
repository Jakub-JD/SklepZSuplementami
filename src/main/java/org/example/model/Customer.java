package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.CustomerDTO;

/**
 * Klasa reprezentująca model klienta w domenie systemu.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry firtName lub lastName będą wartością null.
 */

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

     /**
     * Metoda mapująca dane użytkownika na obiekt CustomerDTO
     */
    public CustomerDTO mapToDTO() {
        return new CustomerDTO(this.firstName, this.lastName, this.email);
    }

    /**
     * Aktualizuje dane klienta na podstawie obiektu transferowego.
     * @throws IllegalArgumentException gdy parametr dto jest wartością null
     * @param dto obiekt transferowy zawierający dane klienta
     */
    public void updateFromDTO(CustomerDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("Nie można zaktualizować danych klienta: przekazany obiekt CustomerDTO jest nullem.");
        }
        this.firstName = dto.firstName();
        this.lastName = dto.lastName();
        this.email = dto.email();
    }
}