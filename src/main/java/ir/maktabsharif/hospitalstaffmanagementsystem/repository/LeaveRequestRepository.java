package ir.maktabsharif.hospitalstaffmanagementsystem.repository;

import ir.maktabsharif.hospitalstaffmanagementsystem.model.LeaveRequest;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.Status;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {

    List<LeaveRequest> findByStaff(User staff);
    List<LeaveRequest> findByStatus(Status status);
    List<LeaveRequest> findByStaffAndStartDateBetween(User staff, LocalDate start, LocalDate end);
    int countByStatus(Status status);

}