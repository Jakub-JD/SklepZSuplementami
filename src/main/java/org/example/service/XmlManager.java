package org.example.service;

import org.example.model.Supplier;
import org.example.exception.InvalidModelDataException;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa odpowiedzialna za zarządzanie zapisem i odczytem danych dostawców do/z plików XML.
 */
public class XmlManager {

    /**
     * Zapisuje listę dostawców do wskazanego pliku XML.
     * @   suppliers lista obiektów klasy Supplier do zapisu
     * @param filePath  ścieżka do pliku docelowego
     */
    public void saveSuppliersToXml(List<Supplier> suppliers, String filePath) {
        validateFilePath(filePath);
        List<Supplier> safeSuppliers = (suppliers != null) ? suppliers : new ArrayList<>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("suppliers");
            doc.appendChild(rootElement);

            for (Supplier supplier : safeSuppliers) {
                Element supplierElement = doc.createElement("supplier");
                rootElement.appendChild(supplierElement);

                appendElement(doc, supplierElement, "companyName", supplier.getCompanyName());
                appendElement(doc, supplierElement, "nip", supplier.getNip());
                appendElement(doc, supplierElement, "city", supplier.getCity());
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas zapisu danych do pliku XML: " + filePath, e);
        }
    }

    /**
     * Odczyt listy dostawców z pliku XML wraz z walidacją danych.
     *
     * @param filePath ścieżka do pliku XML
     * @return lista poprawnie zweryfikowanych dostawców
     * @throws InvalidModelDataException gdy dane w pliku nie spełniają wymagań
     */
    public List<Supplier> readSuppliersFromXml(String filePath) throws InvalidModelDataException {
        validateFilePath(filePath);
        List<Supplier> suppliers = new ArrayList<>();

        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            return suppliers;
        }
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("supplier");

            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String companyName = getElementValue(element, "companyName");
                    String nip = getElementValue(element, "nip");
                    String city = getElementValue(element, "city");

                    if (companyName == null || companyName.trim().isEmpty() ||
                            nip == null || nip.trim().isEmpty()) {
                        throw new InvalidModelDataException("Brak wymaganych danych w pliku XML! Próba utworzenia obiektu (indeks " + i + ") bez nazwy firmy lub NIPu.");
                    }

                    Supplier supplier = Supplier.builder()
                            .companyName(companyName)
                            .nip(nip)
                            .city(city)
                            .build();

                    suppliers.add(supplier);
                }
            }
        } catch (InvalidModelDataException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas odczytu pliku XML: " + filePath, e);
        }
        return suppliers;
    }

    public boolean validateXmlFileHasContent(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.length() > 0;
    }

    public int countSuppliersInList(List<Supplier> suppliers) {
        return suppliers == null ? 0 : suppliers.size();
    }

    private void validateFilePath(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Ścieżka pliku nie może być pusta ani nullem!");
        }
    }

    private void appendElement(Document doc, Element parent, String tagName, String textValue) {
        Element element = doc.createElement(tagName);
        element.appendChild(doc.createTextNode(textValue != null ? textValue : ""));
        parent.appendChild(element);
    }

    private String getElementValue(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}
