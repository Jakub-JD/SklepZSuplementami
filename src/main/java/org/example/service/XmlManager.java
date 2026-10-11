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

public class XmlManager {
    // Zadanie 1: Zapis do pliku XML
    public void saveSuppliersToXml(List<Supplier> suppliers, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Tworzenie głównego elementu <suppliers>
            Element rootElement = doc.createElement("suppliers");
            doc.appendChild(rootElement);

            for (Supplier supplier : suppliers) {
                Element supplierElement = doc.createElement("supplier");
                rootElement.appendChild(supplierElement);

                Element companyName = doc.createElement("companyName");
                companyName.appendChild(doc.createTextNode(supplier.getCompanyName() != null ? supplier.getCompanyName() : ""));
                supplierElement.appendChild(companyName);

                Element nip = doc.createElement("nip");
                nip.appendChild(doc.createTextNode(supplier.getNip() != null ? supplier.getNip() : ""));
                supplierElement.appendChild(nip);

                Element city = doc.createElement("city");
                city.appendChild(doc.createTextNode(supplier.getCity() != null ? supplier.getCity() : ""));
                supplierElement.appendChild(city);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Zapisano dane do pliku XML: " + filePath);

        } catch (Exception e) {
            System.err.println("Błąd podczas zapisu do XML: " + e.getMessage());
        }
    }

    // Zadanie 2: Odczyt z XML i rzucanie wyjątku przy niespełnionych warunkach
    public List<Supplier> readSuppliersFromXml(String filePath) throws InvalidModelDataException {
        List<Supplier> suppliers = new ArrayList<>();
        try {
            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                System.out.println("Plik XML nie istnieje.");
                return suppliers;
            }

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

                    // Zadanie 2: Jeśli brakuje wymaganych danych, przerywamy i rzucamy wyjątek zamiast tworzyć obiekt
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
            System.err.println("Inny błąd podczas odczytu pliku XML: " + e.getMessage());
        }
        return suppliers;
    }

    // Pobierania tekstu z tagów XML
    private String getElementValue(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}
