package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.OrderDTO;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Order {

    private @NonNull String orderNumber;
    private double totalAmount;

    public OrderDTO toDTO() {
        return new OrderDTO(this.orderNumber, this.totalAmount);
    }

    public void restoreFromDTO(OrderDTO dto) {
        this.orderNumber = dto.orderNumber();
        this.totalAmount = dto.totalAmount();
    }
}