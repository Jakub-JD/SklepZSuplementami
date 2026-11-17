package org.example.model;
import java.util.Objects;

/**
 * Model reprezentujący pracownika katedry.
 */
public class StaffMember implements Comparable<StaffMember> {
    private final String fullName;
    private final String department;

    public StaffMember(String fullName, String department) {
        this.fullName = fullName != null ? fullName : "";
        this.department = department != null ? department : "Ogólne / Pozostałe";
    }

    public String getFullName() {
        return fullName;
    }

    public String getDepartment() {
        return department;
    }

    public boolean isDoctor() {
        String lowerName = fullName.toLowerCase();
        return lowerName.contains("dr ");
    }

    @Override
    public int compareTo(StaffMember other) {
        return this.fullName.compareTo(other.fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StaffMember that = (StaffMember) o;
        return fullName.equals(that.fullName) && department.equals(that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName, department);
    }

    @Override
    public String toString() {
        return "- " + fullName;
    }
}
