package org.example;

import org.example.model.Supplement;
import org.example.service.SupplementStore;

import java.util.Arrays;
import java.util.List;
import org.example.model.*;
import org.example.dto.*;

public class Main {
    public static void main(String[] args) {
        //lab1();

        lab3();

    }
    public static void lab1(){
        // 1. Inicjalizacja bazy asortymentu
        List<Supplement> initialInventory = Arrays.asList(
                Supplement.builder().name("Whey Protein 100").category("Białko").price(89.99).build(),
                Supplement.builder().name("Creatine Monohydrate").category("Kreatyna").price(39.99).build(),
                Supplement.builder().name("WPC 80").category("Białko").price(75.50).build(),
                Supplement.builder().name("Pre-Workout X").category("Przedtreningówka").price(120.00).build(),
                Supplement.builder().name("Ashwagandha").category("Witaminy").price(29.99).build()
        );

        List<Supplement> soldItems = Arrays.asList(
                Supplement.builder().name("Whey Protein 100").category("Białko").price(89.99).build(),
                Supplement.builder().name("Creatine Monohydrate").category("Kreatyna").price(39.99).build(),
                Supplement.builder().name("WPC 80").category("Białko").price(75.50).build(),
                Supplement.builder().name("Pre-Workout X").category("Przedtreningówka").price(120.00).build(),
                Supplement.builder().name("Ashwagandha").category("Witaminy").price(29.99).build(),
                Supplement.builder().name("Whey Protein 100").category("Białko").price(89.99).build(),
                Supplement.builder().name("Whey Protein 100").category("Białko").price(89.99).build(),
                Supplement.builder().name("Pre-Workout X").category("Przedtreningówka").price(120.00).build()
        );


        SupplementStore store = new SupplementStore(initialInventory);
        String supplementsFile = "suplementy.txt";

        System.out.println("\n--- 1. Wyszukanie elementów przez filtrowanie ---");
        store.printSupplementsByCategory("Białko");

        System.out.println("\n--- 2. Zliczanie przedmiotów ---");
        store.countSameItems(soldItems);

        System.out.println("\n--- 3. Mapowanie strumienia ---");
        store.printAllNamesMapped();

        System.out.println("\n--- 4. Zapis danych do pliku ---");
        store.saveInventoryToFile(supplementsFile);

        System.out.println("\n--- 5. Filtrowanie i sortowanie danych z pliku ---");
        store.readAndProcessFromFile(supplementsFile, "Białko");
    }

    static public void lab3(){
        System.out.println("\n--- ZADANIE 1 i 2: 3 klasy z Lombokiem (Wzorzec Builder) ---");

        Supplement whey = Supplement.builder()
                .name("Whey Protein 100")
                .category("Białko")
                .price(89.99)
                .build();
        System.out.println("1. Utworzono produkt: " + whey.getName() + " (" + whey.getCategory() + ")");

        Customer customer = Customer.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .email("jan.kowalski@example.com")
                .build();
        System.out.println("2. Utworzono klienta: " + customer.getFirstName() + " " + customer.getLastName());

        Order order = Order.builder()
                .orderNumber("ORD/2026/001")
                .totalAmount(89.99)
                .build();
        System.out.println("3. Utworzono zamówienie: " + order.getOrderNumber());


        System.out.println("\n--- ZADANIE 3: Klasy DTO i wycofywanie zmian ---");
        System.out.println("[STAN PIERWOTNY] Cena produktu " + whey.getName() + " wynosi: " + whey.getPrice() + " PLN");

        // KROK A: Zapis stanu do DTO
        SupplementDTO backupDTO = whey.toDTO();
        System.out.println("-> Zapisano pierwotny stan obiektu do SupplementDTO.");

        // KROK B: Symulacja błędu
        whey.setPrice(9999.99);
        System.out.println("[BŁĄD SYSTEMU] Ktoś omyłkowo zmienił cenę na: " + whey.getPrice() + " PLN");

        // KROK C: Wycofanie zmian przy użyciu DTO
        whey.restoreFromDTO(backupDTO);
        System.out.println("[PO WYCOFANIU ZMIAN] Przywrócono cenę z DTO: " + whey.getPrice() + " PLN");

    }

}