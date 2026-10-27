package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.SupplementDTO;

/**
 * Klasa reprezentująca model suplementu w domenie systemu.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry name lub category będą wartością null.
 */

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Supplement {

    @NonNull
    private String name;
    @NonNull
    private String category;
    private double price;

    /**
     * Metoda mapująca dane suplementu na obiekt SupplementDTO
     */
    public SupplementDTO mapToDTO() {
        return new SupplementDTO(this.name, this.category, this.price);
    }

     /**
     * Aktualizuje dane suplementu na podstawie obiektu transferowego.
     * @throws IllegalArgumentException gdy parametr dto jest wartością null
     * @param dto obiekt transferowy zawierający dane suplementu
     */
    public void updateFromDTO(SupplementDTO dto) {
         if (dto == null) {
            throw new IllegalArgumentException("Nie można zaktualizować danych suplementu: przekazany obiekt SupplementDTO jest nullem.");
        }
        this.name = dto.name();
        this.category = dto.category();
        this.price = dto.price();
    }

    @Override
    public String toString() {
        return name + ";" + category + ";" + price;
    }
}