package ir.maktabsharif.hospitalstaffmanagementsystem.controller;

import ir.maktabsharif.hospitalstaffmanagementsystem.dto.RegisterDto;
import ir.maktabsharif.hospitalstaffmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "registered", required = false) String registered,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", "نام کاربری یا کلمه عبور نادرست است!");
        }
        if (logout != null) {
            model.addAttribute("message", "با موفقیت خارج شدید.");
        }
        if (registered != null) {
            model.addAttribute("message", "ثبت‌نام با موفقیت انجام شد. حالا می‌توانید وارد شوید.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register";
    }


    @PostMapping("/register-process")
    public String register(@ModelAttribute RegisterDto registerDto,
                           Model model,
                           RedirectAttributes redirectAttributes) {


        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            model.addAttribute("error", "کلمه عبور و تأیید آن مطابقت ندارند");
            return "register";
        }

        try {
            userService.registerUser(registerDto);
            redirectAttributes.addFlashAttribute("success",
                    "ثبت‌نام با موفقیت انجام شد. حالا می‌توانید وارد شوید.");
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}