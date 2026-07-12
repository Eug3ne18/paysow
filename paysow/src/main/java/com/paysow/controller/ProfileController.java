package com.paysow.controller;

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
public class ProfileController {

    private final UserService userService;

    @Autowired
    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.findById(userId));
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                 @RequestParam String email,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            userService.updateProfile(userId, fullName, email);
            session.setAttribute("fullName", fullName);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-pin")
    public String changePin(@RequestParam String currentPin,
                             @RequestParam String newPin,
                             @RequestParam String confirmNewPin,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            if (!newPin.equals(confirmNewPin)) {
                redirectAttributes.addFlashAttribute("error", "New PIN and confirmation do not match.");
                return "redirect:/profile";
            }
            userService.changePin(userId, currentPin, newPin);
            redirectAttributes.addFlashAttribute("success", "PIN changed successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profile";
    }
}
