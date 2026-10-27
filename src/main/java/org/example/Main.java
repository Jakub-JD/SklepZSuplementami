package org.example;

import org.example.model.Supplement;
import org.example.service.SupplementStore;
import org.example.model.Supplier;
import org.example.service.XmlManager;
import org.example.exception.InvalidModelDataException;
import org.example.repository.CustomerEntity;
import org.example.repository.CustomerRepository;
import org.example.repository.SupplementEntity;
import org.example.repository.SupplementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Map;

import java.util.Arrays;
import java.util.List;
import org.example.model.*;
import org.example.dto.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        //SpringApplication.run(Main.class, args);
        //lab1();
        //lab2();
        //lab3();

    }
    public static void lab1() {
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
        List<Supplement> proteinSupplements = store.getSupplementsByCategory("Białko");
        proteinSupplements.forEach(s -> System.out.println("- " + s.getName()));

        System.out.println("\n--- 2. Zliczanie przedmiotów ---");
        Map<Supplement, Long> salesCount = store.getSalesCount(soldItems);
        salesCount.forEach((product, count) -> 
                System.out.println("Produkt " + product.getName() + 
                        " (Cena: " + product.getPrice() + ") sprzedano: " + count + " razy")
        );

        System.out.println("\n--- 3. Mapowanie strumienia ---");
        List<String> mappedNames = store.getAllSupplementNames();
        mappedNames.forEach(name -> System.out.println("Nazwa produktu: " + name));

        System.out.println("\n--- 4. Zapis danych do pliku ---");
        try {
            store.saveInventoryToFile(supplementsFile);
            System.out.println("Pomyślnie zapisano asortyment do pliku: " + supplementsFile);
        } catch (IllegalStateException e) {
            System.err.println("Operacja zapisu nie powiodła się: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Operacja zapisu nie powiodła się: " + e.getMessage());
        }

        System.out.println("\n--- 5. Filtrowanie i sortowanie danych z pliku ---");
        try {
            List<String> filteredLines = store.getFilteredLinesFromFile(supplementsFile, "Białko");
            filteredLines.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.err.println("Operacja odczytu nie powiodła się: " + e.getMessage());
        }
    }

    public static void lab2() {
        System.out.println("\n--- ZADANIE 1 i 2: Zapis i odczyt danych z XML z walidacją wyjątkami ---");

        List<Supplier> suppliersList = Arrays.asList(
                Supplier.builder().companyName("KFD").nip("1234567890").city("Lublin").build(),
                Supplier.builder().companyName("SFD").nip("9876543210").city("Radom").build()
        );

        XmlManager xmlManager = new XmlManager();
        String xmlFilePath = "dostawcy.xml";

        // 1. Zapis listy do XML
        System.out.println("-> Zapisywanie listy dostawców do XML...");
        xmlManager.saveSuppliersToXml(suppliersList, xmlFilePath);

        // 2. Odczyt z XML i wyświetlenie poprawnych danych
        System.out.println("\n-> Odczytywanie poprawnego pliku XML:");
        try {
            List<Supplier> loadedSuppliers = xmlManager.readSuppliersFromXml(xmlFilePath);
            System.out.println("Lista odczytanych obiektów:");
            loadedSuppliers.forEach(s -> System.out.println(" - " + s.getCompanyName() + " (NIP: " + s.getNip() + ") z miasta: " + s.getCity()));
        } catch (InvalidModelDataException e) {
            System.err.println("Błąd: " + e.getMessage());
        }

        // 3. Symulacja błędnego XML (test wyjątku dla 2 zadania)
        System.out.println("\n-> Symulacja odczytu uszkodzonych danych (wymuszenie wyjątku)...");
        List<Supplier> badSuppliersList = Arrays.asList(
                Supplier.builder().companyName("").nip("123456789").city("Swidnik").build()
        );
        String badXmlPath = "uszkodzeni_dostawcy.xml";
        xmlManager.saveSuppliersToXml(badSuppliersList, badXmlPath);

        try {
            // Próba odczytu z pliku, w którym brakuje nazwy firmy
            xmlManager.readSuppliersFromXml(badXmlPath);
        } catch (InvalidModelDataException e) {
            System.err.println("Złapano oczekiwany wyjątek z powodu braków w XML:");
            System.err.println(e.getMessage());
        }
    }

    public static void lab3() {
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


        // --- NOWOŚĆ: Polecenie 4.2 i 4.4 (Obsługa wyjątków z Lomboka) ---
        System.out.println("\n--- TEST BŁĘDÓW 1: Walidacja @NonNull podczas tworzenia ---");
        try {
            System.out.println("Próba utworzenia klienta bez imienia (null)...");
            Customer invalidCustomer = Customer.builder()
                    .firstName(null) // To wywoła wyjątek
                    .lastName("Nowak")
                    .build();
        } catch (NullPointerException e) {
            System.err.println("Złapano oczekiwany wyjątek (Lombok): " + e.getMessage());
        }


        System.out.println("\n--- ZADANIE 3: Klasy DTO i wycofywanie zmian ---");
        System.out.println("[STAN PIERWOTNY] Cena produktu " + whey.getName() + " wynosi: " + whey.getPrice() + " PLN");

        // KROK A: Zapis stanu do DTO
        SupplementDTO backupDTO = whey.mapToDTO();
        System.out.println("-> Zapisano pierwotny stan obiektu do SupplementDTO.");

        // KROK B: Symulacja błędu
        whey.setPrice(9999.99);
        System.out.println("[BŁĄD SYSTEMU] Ktoś omyłkowo zmienił cenę na: " + whey.getPrice() + " PLN");

        // KROK C: Wycofanie zmian przy użyciu DTO
        whey.updateFromDTO(backupDTO);
        System.out.println("[PO WYCOFANIU ZMIAN] Przywrócono cenę z DTO: " + whey.getPrice() + " PLN");


        System.out.println("\n--- TEST BŁĘDÓW 2: Walidacja null w metodzie updateFromDTO ---");
        try {
            System.out.println("Próba przywrócenia danych z uszkodzonego (null) pliku/DTO...");
            whey.updateFromDTO(null); 
        } catch (IllegalArgumentException e) {
            System.err.println("Złapano oczekiwany wyjątek (Logika biznesowa): " + e.getMessage());
        }

        System.out.println("\n--- TEST BŁĘDÓW 3: Stworzenie zlego obiektu ---");
        try {
            Customer customer2 = Customer.builder()
                    .email("jan.kowalski@example.com")
                    .build();
        } catch (Exception e) {
            System.err.println("Złapano oczekiwany wyjątek tworzenia obiektu: " + e.getMessage());
        }
    }

    @Bean
    public CommandLineRunner runJdbcDemo(CustomerRepository customerRepo, SupplementRepository supplementRepo) {
        return args -> {
            System.out.println("\nModuł klienta");

            // 1. zapis danych o klientach
            System.out.println("1. Dodawanie klientów do bazy");
            customerRepo.save(CustomerEntity.builder().firstName("Jakub").lastName("Fus").email("jakub@fus.pl").active(true).build());
            customerRepo.save(CustomerEntity.builder().firstName("Jakub").lastName("Gwozdowski").email("jakub@gwozdowski.pl").active(true).build());
            customerRepo.save(CustomerEntity.builder().firstName("Jan").lastName("Kowalski").email("jan@kowalski.pl").active(true).build());

            // 2. odczyt danych o klientach i wybranym kliencie
            System.out.println("\n2. Lista aktywnych klientów");
            customerRepo.findAllActive().forEach(c ->
                    System.out.println("ID: " + c.getId() + " | " + c.getFirstName() + " " + c.getLastName())
            );
            System.out.println("Klient o wybranym ID: " + customerRepo.findById(2L).getId() +" "+ customerRepo.findById(2L).getFirstName() +" "+ customerRepo.findById(2L).getLastName()+" "+ customerRepo.findById(2L).getEmail());

            // 3. aktualizacja danych klientów
            System.out.println("\n3. Aktualizacja customer (ID: 2)");
            CustomerEntity update = CustomerEntity.builder().firstName("Teresa").lastName("Kowalska").email("teresa@kowal.xyz").build();
            customerRepo.update(2L, update);

            System.out.println("Odczyt po edycji: " + customerRepo.findById(2L).getEmail());

            // 4. Usuwanie miękkie (Dezaktywacja) i permanentne
            System.out.println("\n4. Usuwanie klientów");
            System.out.println("-> Dezaktywacja Jakub Fus (ID: 1)...");
            customerRepo.deactivate(1L);

            System.out.println("-> Permanentne usunięcie Jan Kowalski (ID: 3)...");
            customerRepo.deletePermanently(3L);

            System.out.println("\nLista aktywnych klientów po usuwaniu:");
            customerRepo.findAllActive().forEach(c ->
                    System.out.println("ID: " + c.getId() + " | " + c.getFirstName() + " " + c.getLastName())
            );


            System.out.println("\n-> Suplementy");
            supplementRepo.save(SupplementEntity.builder().name("Kreatyna").price(50.0).build());
            supplementRepo.updatePrice(1L, 45.0);
            supplementRepo.findAll().forEach(s -> System.out.println(s.getName() + " - " + s.getPrice() + " PLN"));
        };
    }

}