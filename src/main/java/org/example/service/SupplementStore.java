package org.example.service;
import org.example.model.Supplement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class SupplementStore {

    private List<Supplement> inventory;

    public SupplementStore(List<Supplement> inventory) {
        this.inventory = inventory;
    }

    // 1. Filtrowanie po kategorii
    public void printSupplementsByCategory(String category) {
        inventory.stream()
                .filter(s -> s.getCategory().toLowerCase().equals(category.toLowerCase()))
                .forEach(s -> System.out.println("- " + s.getName()));
    }

    // 2. Zliczanie powtorzen
    public void countSameItems(List<Supplement> items) {
        items.stream()
                .distinct()
                .forEach(unikalnyProdukt -> {
                    long liczbaWystapien = items.stream()
                            .filter(s -> s.equals(unikalnyProdukt))
                            .count();
                    System.out.println("Produkt " + unikalnyProdukt.getName() +
                            " (Cena: " + unikalnyProdukt.getPrice() + ") sprzedano: " + liczbaWystapien + " razy");
                });
    }

    // 3. Mapowanie nazw
    public void printAllNamesMapped() {
        inventory.stream()
                .map(Supplement::getName)
                .forEach(name -> System.out.println("Nazwa produktu: " + name));
    }

    // 4. Zapis do pliku (java.io)
    public void saveInventoryToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Supplement s : inventory) {
                writer.write(s.toString());
                writer.newLine();
            }
            System.out.println("Pomyślnie zapisano asortyment do pliku: " + fileName);
        } catch (IOException e) {
            System.err.println("Błąd zapisu pliku: " + e.getMessage());
        }
    }

    // 5. Odczyt i filtrowanie (java.nio)
    public void readAndProcessFromFile(String fileName, String filterKeyword) {
        Path path = Paths.get(fileName);
        try {
            Files.lines(path)
                    .filter(line -> line.contains(filterKeyword))
                    .sorted()
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Błąd odczytu pliku: " + e.getMessage());
        }
    }
}