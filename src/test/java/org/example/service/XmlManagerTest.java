package org.example.service;

import org.example.exception.InvalidModelDataException;
import org.example.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlManagerTest {

    private XmlManager xmlManager;

    @BeforeEach
    void setUp() {
        xmlManager = new XmlManager();
    }

    @Test
    void testSaveAndReadSuppliers_Success() throws Exception {
        Path xmlFile = Files.createTempFile("suppliers", ".xml");
        List<Supplier> suppliers = Arrays.asList(
                new Supplier("Firma A", "1234567890", "Warszawa"),
                new Supplier("Firma B", "0987654321", "Kraków")
        );

        assertDoesNotThrow(() -> xmlManager.saveSuppliersToXml(suppliers, xmlFile.toString()));
        assertTrue(xmlManager.validateXmlFileHasContent(xmlFile.toString()));

        List<Supplier> loaded = xmlManager.readSuppliersFromXml(xmlFile.toString());
        assertEquals(2, loaded.size());
        assertEquals("Firma A", loaded.get(0).getCompanyName());
        Files.deleteIfExists(xmlFile);
    }

    @Test
    void testSaveSuppliersToXml_EmptyList() throws Exception {
        Path xmlFile = Files.createTempFile("empty_suppliers", ".xml");
        List<Supplier> suppliers = new ArrayList<>();

        assertDoesNotThrow(() -> xmlManager.saveSuppliersToXml(suppliers, xmlFile.toString()));
        List<Supplier> loaded = xmlManager.readSuppliersFromXml(xmlFile.toString());
        assertTrue(loaded.isEmpty());
        Files.deleteIfExists(xmlFile);
    }

    @Test
    void testSaveSuppliersToXml_NullList() throws IOException {
        Path xmlFile = Files.createTempFile("null_suppliers", ".xml");
        assertDoesNotThrow(() -> xmlManager.saveSuppliersToXml(null, xmlFile.toString()));
        Files.deleteIfExists(xmlFile);
    }

    @Test
    void testReadSuppliersFromXml_FileNotFound() throws InvalidModelDataException {
        List<Supplier> result = xmlManager.readSuppliersFromXml("non_existent_file_abc.xml");
        assertTrue(result.isEmpty());
    }

    @Test
    void testReadSuppliersFromXml_InvalidDataMissingName() throws IOException {
        Path badXml = Files.createTempFile("bad_name", ".xml");
        String badContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><suppliers><supplier><companyName></companyName><nip>12345</nip><city>Lodz</city></supplier></suppliers>";
        Files.writeString(badXml, badContent);

        assertThrows(InvalidModelDataException.class, () -> {
            xmlManager.readSuppliersFromXml(badXml.toString());
        });
        Files.deleteIfExists(badXml);
    }

    @Test
    void testReadSuppliersFromXml_InvalidDataMissingNip() throws IOException {
        Path badXml = Files.createTempFile("bad_nip", ".xml");
        String badContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><suppliers><supplier><companyName>Firma X</companyName><nip></nip><city>Lodz</city></supplier></suppliers>";
        Files.writeString(badXml, badContent);

        assertThrows(InvalidModelDataException.class, () -> {
            xmlManager.readSuppliersFromXml(badXml.toString());
        });
        Files.deleteIfExists(badXml);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testSaveSuppliersToXml_InvalidPath(String path) {
        List<Supplier> list = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            xmlManager.saveSuppliersToXml(list, path);
        });
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testReadSuppliersFromXml_InvalidPath(String path) {
        assertThrows(IllegalArgumentException.class, () -> {
            xmlManager.readSuppliersFromXml(path);
        });
    }

    @Test
    void testValidateXmlFileHasContent_NonExistent() {
        assertFalse(xmlManager.validateXmlFileHasContent("non_existent_file_xyz.xml"));
    }

    @Test
    void testCountSuppliersInList() {
        List<Supplier> list = Arrays.asList(new Supplier("A", "1", "X"));
        assertEquals(1, xmlManager.countSuppliersInList(list));
        assertEquals(0, xmlManager.countSuppliersInList(null));
    }

}