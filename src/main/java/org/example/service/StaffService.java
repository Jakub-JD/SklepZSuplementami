package org.example.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.example.model.StaffMember;
import org.example.repository.StaffRepository;

public class StaffService {
    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    /**
     * Zwraca pracowników pogrupowanych według zakładów i posortowanych alfabetycznie.
     */
    public Map<String, List<StaffMember>> getStaffGroupedAndSorted() {
        List<StaffMember> allStaff = staffRepository.fetchStaffMembers();
        return allStaff.stream()
                .sorted()
                .collect(Collectors.groupingBy(
                        StaffMember::getDepartment,
                        Collectors.toList()
                ));
    }

    /**
     * Zwraca pracowników posiadających stopień dr lub dr inż., posortowanych alfabetycznie.
     */
    public Map<String, List<StaffMember>> getDoctorsGroupedAndSorted() {
        List<StaffMember> allStaff = staffRepository.fetchStaffMembers();
        return allStaff.stream()
                .filter(StaffMember::isDoctor)
                .sorted()
                .collect(Collectors.groupingBy(
                        StaffMember::getDepartment,
                        Collectors.toList()
                ));
    }
}
