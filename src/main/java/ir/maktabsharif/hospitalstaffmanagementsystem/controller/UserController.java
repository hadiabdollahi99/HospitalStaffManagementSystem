package ir.maktabsharif.hospitalstaffmanagementsystem.controller;

import ir.maktabsharif.hospitalstaffmanagementsystem.dto.LeaveRequestDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.LeaveRequest;
import ir.maktabsharif.hospitalstaffmanagementsystem.model.User;
import ir.maktabsharif.hospitalstaffmanagementsystem.service.LeaveRequestService;
import ir.maktabsharif.hospitalstaffmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LeaveRequestService leaveRequestService;


    @GetMapping("/dashboard")
    public String home(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.getCurrentUser(username);
        model.addAttribute("user", user);

        return "user/dashboard";
    }


    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            User user = userService.getCurrentUser(username);


            long total = user.getLeaveRequests() != null ? user.getLeaveRequests().size() : 0;
            long pending = 0;
            long approved = 0;
            long rejected = 0;

            if (user.getLeaveRequests() != null) {
                pending = user.getLeaveRequests().stream()
                        .filter(lr -> lr.getStatus() != null && lr.getStatus().name().equals("PENDING"))
                        .count();
                approved = user.getLeaveRequests().stream()
                        .filter(lr -> lr.getStatus() != null && lr.getStatus().name().equals("APPROVE"))
                        .count();
                rejected = user.getLeaveRequests().stream()
                        .filter(lr -> lr.getStatus() != null && lr.getStatus().name().equals("REJECT"))
                        .count();
            }

            model.addAttribute("user", user);
            model.addAttribute("total", total);
            model.addAttribute("pending", pending);
            model.addAttribute("approved", approved);
            model.addAttribute("rejected", rejected);

            return "user/profile";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error";
        }
    }

    @GetMapping("/leave-requests")
    public String viewLeaveRequests(Authentication authentication, Model model) {
        String username = authentication.getName();
        List<LeaveRequest> leaveRequests = leaveRequestService.getUserLeaveRequests(username);
        model.addAttribute("leaveRequests", leaveRequests);
        return "user/leave-requests";
    }

    @GetMapping("/leave-requests/new")
    public String showLeaveRequestForm(Model model) {
        model.addAttribute("leaveRequest", new LeaveRequestDto());
        return "user/leave-request-form";
    }

    @PostMapping("/leave-requests")
    public String submitLeaveRequest(@ModelAttribute("leaveRequest") LeaveRequestDto leaveRequestDto,
                                     Authentication authentication) {
        String username = authentication.getName();
        leaveRequestService.submitLeaveRequest(leaveRequestDto, username);
        return "redirect:/user/leave-requests?success";
    }
}