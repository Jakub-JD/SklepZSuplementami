package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.OrderDTO;

/**
 * Klasa reprezentująca model zamówienia w domenie systemu.
 * @throws NullPointerException jeśli podczas tworzenia obiektu
 * parametr orederNumber będzie wartością null.
 */

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Order {

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