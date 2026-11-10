package org.example.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StaffService {
    public static void StaffFunc(){
        String url = "https://cs.pollub.pl/staff/";

        try {
            System.out.println("Pobieranie danych ze strony: " + url);
            Document doc = Jsoup.connect(url).get();

            Map<String, List<String>> staffByDepartment = new LinkedHashMap<>();

            String currentDepartment = "Ogólne / Pozostałe";
            staffByDepartment.put(currentDepartment, new ArrayList<>());

            Elements elements = doc.select( "h3, a");

            for (Element el : elements) {
                String text = el.text().trim();
                if (text.isEmpty()) continue;

                String lowerText = text.toLowerCase();

                if (lowerText.contains("zakład") || lowerText.contains("pracownia techniczna") || lowerText.contains("sekretariat") || lowerText.contains("kierownik katedry")) {
                    currentDepartment = text;
                    staffByDepartment.putIfAbsent(currentDepartment, new ArrayList<>());
                }
                else if (lowerText.startsWith("dr") || lowerText.startsWith("mgr") || lowerText.contains("prof.") || lowerText.startsWith("inż.")) {
                    List<String> departmentList = staffByDepartment.get(currentDepartment);
                    if (!departmentList.contains(text)) {
                        departmentList.add(text);
                    }
                }
            }

            System.out.println("  LISTA PRACOWNIKÓW KATEDRY INFORMATYKI PL");
            System.out.println("==============================================");

            for (Map.Entry<String, List<String>> entry : staffByDepartment.entrySet()) {
                String department = entry.getKey();
                List<String> employees = entry.getValue();

                if (!employees.isEmpty()) {
                    Collections.sort(employees);

                    System.out.println("\n " + department);
                    System.out.println("----------------------------------------------");
                    for (String employee : employees) {
                        System.out.println("- " +employee);
                    }
                }
            }

            System.out.println("\n  LISTA PRACOWNIKÓW (STOPNIU DR / DR INŻ.)");
            System.out.println("==============================================");

            for (Map.Entry<String, List<String>> entry : staffByDepartment.entrySet()) {
                String department = entry.getKey();
                List<String> employees = entry.getValue();

                List<String> filteredEmployees = employees.stream()
                        .filter(emp -> {
                            String lEmp = emp.toLowerCase();
                            return lEmp.contains("dr ");
                        })
                        .sorted()
                        .collect(Collectors.toList());

                if (!filteredEmployees.isEmpty()) {
                    System.out.println("\n " + department);
                    System.out.println("----------------------------------------------");
                    for (String employee : filteredEmployees) {
                        System.out.println("- " + employee);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas połączenia lub parsowania strony: " + e.getMessage());
        }
    }
}
