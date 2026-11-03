package org.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.SupplementDTO;

/**
 * Klasa reprezentująca model suplementu w domenie systemu.
 *
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametry name lub category będą wartością null.
 */

@Entity
@Table(name = "supplements")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class Supplement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String name;

    @Column(nullable = false)
    @NonNull
    private String category;

    private double price;

    public SupplementDTO mapToDTO() {
        return new SupplementDTO(this.name, this.category, this.price);
    }

    /**
     * @throws IllegalArgumentException gdy parametr dto jest wartością null
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