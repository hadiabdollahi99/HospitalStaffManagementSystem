package ir.maktabsharif.hospitalstaffmanagementsystem.controller;

import ir.maktabsharif.hospitalstaffmanagementsystem.dto.LeaveDecisionDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.dto.UserUpdateDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.LeaveRequest;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import ir.maktabsharif.hospitalstaffmanagementsystem.service.LeaveRequestService;
import ir.maktabsharif.hospitalstaffmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final LeaveRequestService leaveRequestService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalStaff = userService.getTotalStaffCount();
        int pendingRequests = leaveRequestService.getPendingRequestsCount();

        model.addAttribute("totalStaff", totalStaff);
        model.addAttribute("pendingRequests", pendingRequests);

        return "admin/dashboard";
    }

    @GetMapping("/staff")
    public String listStaff(Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<User> staff = userService.getAllUsers();

        model.addAttribute("staff", staff);
        model.addAttribute("currentPage", page);
        return "admin/staff-list";
    }

    @GetMapping("/staff/{id}")
    public String viewStaffDetails(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            model.addAttribute("staff", user.get());
            return "admin/staff-details";
        }
        return "redirect:/admin/staff?error";
    }

    @GetMapping("/staff/{id}/edit")
    public String showEditStaffForm(@PathVariable Long id, Model model) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            UserUpdateDto updateDto = new UserUpdateDto();
            updateDto.setName(user.get().getName());
            updateDto.setContactNumber(user.get().getContactNumber());
            updateDto.setJobTitle(user.get().getJobTitle());
            updateDto.setDepartment(user.get().getDepartment());

            model.addAttribute("user", updateDto);
            model.addAttribute("staffId", id);
            return "admin/edit-staff";
        }
        return "redirect:/admin/staff?error";
    }

    @PostMapping("/staff/{id}/edit")
    public String updateStaff(@PathVariable Long id,
                              @ModelAttribute UserUpdateDto updateDto) {
        User user = new User();
        user.setName(updateDto.getName());
        user.setContactNumber(updateDto.getContactNumber());
        user.setJobTitle(updateDto.getJobTitle());
        user.setDepartment(updateDto.getDepartment());

        userService.updateUser(id, user);
        return "redirect:/admin/staff/" + id + "?success";
    }

    @PostMapping("/staff/{id}/delete")
    public String deleteStaff(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/staff?deleted";
    }

    @GetMapping("/leave-requests")
    public String viewAllLeaveRequests(Model model) {
        List<LeaveRequest> pendingRequests = leaveRequestService.getPendingLeaveRequests();
        model.addAttribute("leaveRequests", pendingRequests);
        return "admin/leave-requests-list";
    }

    @GetMapping("/leave-requests/{id}")
    public String viewLeaveRequestDetails(@PathVariable Long id, Model model) {
        Optional<LeaveRequest> leaveRequest = leaveRequestService.getLeaveRequestById(id);
        if (leaveRequest.isPresent()) {
            model.addAttribute("leaveRequest", leaveRequest.get());
            model.addAttribute("decision", new LeaveDecisionDto());
            return "admin/leave-request-details";
        }
        return "redirect:/admin/leave-requests?error";
    }

    @PostMapping("/leave-requests/{id}/approve")
    public String approveLeaveRequest(@PathVariable Long id,
                                      @ModelAttribute LeaveDecisionDto decision) {
        leaveRequestService.approveLeaveRequest(id, decision.getComment());
        return "redirect:/admin/leave-requests?approved";
    }

    @PostMapping("/leave-requests/{id}/reject")
    public String rejectLeaveRequest(@PathVariable Long id,
                                     @ModelAttribute LeaveDecisionDto decision) {
        leaveRequestService.rejectLeaveRequest(id, decision.getComment());
        return "redirect:/admin/leave-requests?rejected";
    }

    @GetMapping("/search")
    public String searchStaff(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String department,
                              @RequestParam(required = false) String jobTitle,
                              Model model) {
        List<User> results = userService.searchUsers(name, department, jobTitle);
        model.addAttribute("staff", results);
        model.addAttribute("searchName", name);
        model.addAttribute("searchDepartment", department);
        model.addAttribute("searchJobTitle", jobTitle);
        return "admin/search-results";
    }
}