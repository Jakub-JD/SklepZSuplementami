package org.example.repository;

import org.example.model.StaffMember;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsoupStaffRepository implements StaffRepository {
    private static final String STAFF_URL = "https://cs.pollub.pl/staff/";

    @Override
    public List<StaffMember> fetchStaffMembers() {
        List<StaffMember> staffList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(STAFF_URL).get();
            Elements elements = doc.select("h3, a");

            String currentDepartment = "Ogólne / Pozostałe";

            for (Element el : elements) {
                String text = el.text().trim();
                if (text.isEmpty()) continue;

                String lowerText = text.toLowerCase();
                if (isDepartmentHeader(lowerText)) {
                    currentDepartment = text;
                } else if (isStaffMemberName(lowerText)) {
                    staffList.add(new StaffMember(text, currentDepartment));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Nie udało się pobrać listy pracowników ze strony: " + STAFF_URL, e);
        }
        return staffList;
    }

    private boolean isDepartmentHeader(String text) {
        return text.contains("zakład") ||
                text.contains("pracownia techniczna") ||
                text.contains("sekretariat") ||
                text.contains("kierownik katedry");
    }

    private boolean isStaffMemberName(String text) {
        return text.startsWith("dr") ||
                text.startsWith("mgr") ||
                text.contains("prof.") ||
                text.startsWith("inż.");
    }
}