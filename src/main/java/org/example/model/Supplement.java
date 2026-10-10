package org.example.model;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.example.dto.SupplementDTO;

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

    public SupplementDTO toDTO() {
        return new SupplementDTO(this.name, this.category, this.price);
    }

    public void restoreFromDTO(SupplementDTO dto) {
        this.name = dto.name();
        this.category = dto.category();
        this.price = dto.price();
    }

    @Override
    public String toString() {
        return name + ";" + category + ";" + price;
    }
}