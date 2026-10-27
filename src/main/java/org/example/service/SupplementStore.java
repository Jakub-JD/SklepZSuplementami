package org.example.service;
import org.example.model.Supplement;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class SupplementStore {

    private List<Supplement> inventory;

    public SupplementStore(List<Supplement> inventory) {
        this.inventory = inventory;
    }

    /**
     * @param category nazwa kategorii
     * @return lista suplementów w danej kategorii
     */
    public List<Supplement> getSupplementsByCategory(String category) {
        return inventory.stream()
                .filter(s -> s.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * @param items lista sprzedanych produktów (mogą się powtarzać)
     * @return mapa z produktem jako kluczem i liczbą wystąpień jako wartością
     */
    public Map<Supplement, Long> getSalesCount(List<Supplement> items) {
        return items.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    }

    /**
     * @return lista nazw wszystkich suplementów
     */
    public List<String> getAllSupplementNames() {
        return inventory.stream()
                .map(Supplement::getName)
                .collect(Collectors.toList());
    }

    /**
     * @param fileName nazwa pliku docelowego
     * @throws IllegalStateException gdy zapis do pliku się nie powiedzie
     * @throws IllegalArgumentException gdy nazwa pliku ma wartosc null lub jest pusta
     */
    public void saveInventoryToFile(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Nazwa pliku nie może być pusta ani nullem!");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Supplement s : inventory) {
                writer.write(s.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nie udało się zapisać asortymentu do pliku: " + fileName, e);
        }
    }

    /**
     * @param fileName nazwa pliku źródłowego
     * @param filterKeyword słowo kluczowe do wyszukania
     * @return posortowana lista linii spełniających kryteria
     * @throws IllegalArgumentException gdy plik nie istnieje lub nie można go odczytać
     */
    public List<String> getFilteredLinesFromFile(String fileName, String filterKeyword) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("Nazwa pliku nie może być pusta ani nullem!");
        }
        Path path = Paths.get(fileName);
        try {
            return Files.lines(path)
                    .filter(line -> line.contains(filterKeyword))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException("Błąd odczytu. Upewnij się, że plik istnieje: " + fileName, e);
        }
    }
}