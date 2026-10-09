package org.example;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1. Inicjalizacja bazy asortymentu
        List<Supplement> initialInventory = Arrays.asList(
                new Supplement("Whey Protein 100", "Białko", 89.99),
                new Supplement("Creatine Monohydrate", "Kreatyna", 39.99),
                new Supplement("WPC 80", "Białko", 75.50),
                new Supplement("Pre-Workout X", "Przedtreningówka", 120.00),
                new Supplement("Ashwagandha", "Witaminy", 29.99)
        );

        List<Supplement> soldItems = Arrays.asList(
                new Supplement("Whey Protein 100", "Białko", 89.99),
                new Supplement("Creatine Monohydrate", "Kreatyna", 39.99),
                new Supplement("WPC 80", "Białko", 75.50),
                new Supplement("Pre-Workout X", "Przedtreningówka", 120.00),
                new Supplement("Ashwagandha", "Witaminy", 29.99),
                new Supplement("Whey Protein 100", "Białko", 89.99),
                new Supplement("Whey Protein 100", "Białko", 89.99),
                new Supplement("Pre-Workout X", "Przedtreningówka", 120.00)
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
}