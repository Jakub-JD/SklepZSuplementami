package org.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

import org.example.model.Supplement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


class SupplementStoreTest {
    private SupplementStore store;
    private List<Supplement> sampleInventory;

    @BeforeEach
    void setUp() {
        sampleInventory = Arrays.asList(
                Supplement.builder().name("Whey Protein").category("Białko").price(89.99).build(),
                Supplement.builder().name("Creatine").category("Kreatyna").price(39.99).build(),
                Supplement.builder().name("WPC 80").category("Białko").price(75.00).build()
        );
        store = new SupplementStore(sampleInventory);
    }

    @Test
    void testGetSupplementsByCategory_ValidCategory() {
        List<Supplement> proteinList = store.getSupplementsByCategory("Białko");
        assertEquals(2, proteinList.size());
    }

    @Test
    void testGetSupplementsByCategory_NullCategory() {
        List<Supplement> result = store.getSupplementsByCategory(null);
        assertTrue(result.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Białko", "Kreatyna", "NonExistent"})
    void testGetSupplementsByCategory_Parameterized(String category) {
        List<Supplement> result = store.getSupplementsByCategory(category);
        assertNotNull(result);
    }

    @Test
    void testGetSalesCount() {
        List<Supplement> sold = Arrays.asList(sampleInventory.get(0), sampleInventory.get(0), sampleInventory.get(1));
        Map<Supplement, Long> counts = store.getSalesCount(sold);
        assertEquals(2L, counts.get(sampleInventory.get(0)));
        assertEquals(1L, counts.get(sampleInventory.get(1)));
    }

    @Test
    void testGetSalesCount_NullList() {
        Map<Supplement, Long> counts = store.getSalesCount(null);
        assertTrue(counts.isEmpty());
    }

    @Test
    void testGetAllSupplementNames() {
        List<String> names = store.getAllSupplementNames();
        assertEquals(3, names.size());
        assertTrue(names.contains("Whey Protein"));
    }

    @ParameterizedTest
    @CsvSource({
            "50.0, 1",
            "100.0, 3",
            "10.0, 0"
    })
    void testGetSupplementsCheaperThan(double maxPrice, int expectedCount) {
        List<Supplement> cheap = store.getSupplementsCheaperThan(maxPrice);
        assertEquals(expectedCount, cheap.size());
    }

    @Test
    void testHasSupplement() {
        assertTrue(store.hasSupplement("Creatine"));
        assertFalse(store.hasSupplement("NonExistent"));
        assertFalse(store.hasSupplement(null));
    }

    @Test
    void testSaveInventoryToFile_Success() throws IOException {
        Path file = Files.createTempFile("test_inv", ".txt");
        assertDoesNotThrow(() -> store.saveInventoryToFile(file.toString()));
        Files.deleteIfExists(file);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void testSaveInventoryToFile_InvalidFileName(String badFileName) {
        assertThrows(IllegalArgumentException.class, () -> store.saveInventoryToFile(badFileName));
    }

    @Test
    void testSaveInventoryToFile_NullFileName() {
        assertThrows(IllegalArgumentException.class, () -> store.saveInventoryToFile(null));
    }

    @Test
    void testGetFilteredLinesFromFile_Success(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("filtered.txt");
        store.saveInventoryToFile(file.toString());

        List<String> lines = store.getFilteredLinesFromFile(file.toString(), "Białko");
        assertEquals(2, lines.size());
    }

    @Test
    void testGetFilteredLinesFromFile_NonExistentFile() {
        assertThrows(IllegalArgumentException.class, () -> store.getFilteredLinesFromFile("bad_path_xyz.txt", "test"));
    }

}