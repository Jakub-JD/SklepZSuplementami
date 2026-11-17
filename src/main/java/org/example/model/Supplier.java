package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa reprezentująca dostawcę w systemie.
 * Wykorzystuje Lomboka do redukcji kodu.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {
    private String companyName;
    private String nip;
    private String city;
}
