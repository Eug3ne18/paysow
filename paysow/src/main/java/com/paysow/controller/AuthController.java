package com.paysow.controller;

import com.paysow.model.User;
import com.paysow.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phoneNumber,
                          @RequestParam String pin,
                          @RequestParam String confirmPin,
                          RedirectAttributes redirectAttributes) {
        try {
            if (!pin.equals(confirmPin)) {
                redirectAttributes.addFlashAttribute("error", "PIN and confirmation PIN do not match.");
                return "redirect:/signup";
            }
            userService.signup(fullName, email, phoneNumber, pin);
            redirectAttributes.addFlashAttribute("success", "Account created! You can now log in.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/signup";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String phoneNumber,
                         @RequestParam String pin,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(phoneNumber, pin);
            session.setAttribute("userId", user.getId());
            session.setAttribute("fullName", user.getFullName());
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
