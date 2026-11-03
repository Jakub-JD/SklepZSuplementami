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
import org.example.dto.OrderDTO;

/**
 * Klasa reprezentująca model zamówienia w domenie systemu.
 *
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametr orderNumber będzie wartością null.
 */

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private @NonNull String orderNumber;

    private double totalAmount;

    public OrderDTO mapToDTO() {
        return new OrderDTO(this.orderNumber, this.totalAmount);
    }

    /**
     * @throws IllegalArgumentException gdy parametr dto jest wartością null
     */
    public void updateFromDTO(OrderDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Nie można zaktualizować danych zamówienia: przekazany obiekt OrderDTO jest nullem.");
        }
        this.orderNumber = dto.orderNumber();
        this.totalAmount = dto.totalAmount();
    }
}