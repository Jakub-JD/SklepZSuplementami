package org.example.repository;

import org.example.model.StaffMember;
import java.util.List;

public interface StaffRepository {
    List<StaffMember> fetchStaffMembers();
}
