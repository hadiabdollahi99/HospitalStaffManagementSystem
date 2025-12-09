package ir.maktabsharif.hospitalstaffmanagementsystem.service;

import ir.maktabsharif.hospitalstaffmanagementsystem.dto.LeaveRequestDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.LeaveRequest;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.Status;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import ir.maktabsharif.hospitalstaffmanagementsystem.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserService userService;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               UserService userService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userService = userService;
    }

    public LeaveRequest submitLeaveRequest(LeaveRequestDto leaveRequestDto, String username) {
        User staff = userService.getCurrentUser(username);

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .startDate(leaveRequestDto.getStartDate())
                .endDate(leaveRequestDto.getEndDate())
                .description(leaveRequestDto.getDescription())
                .status(Status.PENDING)
                .submittedAt(LocalDateTime.now())
                .staff(staff)
                .build();

        return leaveRequestRepository.save(leaveRequest);
    }

    public List<LeaveRequest> getUserLeaveRequests(String username) {
        User staff = userService.getCurrentUser(username);
        return leaveRequestRepository.findByStaff(staff);
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(Status.PENDING);
    }

    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id);
    }

    public LeaveRequest approveLeaveRequest(Long id, String comment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus(Status.APPROVE);
        leaveRequest.setManagerComment(comment);
        leaveRequest.setReviewedAt(LocalDateTime.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest rejectLeaveRequest(Long id, String comment) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        leaveRequest.setStatus(Status.REJECT);
        leaveRequest.setManagerComment(comment);
        leaveRequest.setReviewedAt(LocalDateTime.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    public int getPendingRequestsCount() {
        return leaveRequestRepository.countByStatus(Status.PENDING);
    }
}
