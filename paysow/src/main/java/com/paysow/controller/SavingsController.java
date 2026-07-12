package com.paysow.controller;

import com.paysow.service.UserService;
import com.paysow.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SavingsController {

    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public SavingsController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/savings")
    public String savingsPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.findById(userId));
        return "savings";
    }

    @PostMapping("/savings/deposit")
    public String deposit(@RequestParam double amount, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            walletService.depositToSavings(userId, amount);
            redirectAttributes.addFlashAttribute("success", "Moved to Savings successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/savings";
    }

    @PostMapping("/savings/withdraw")
    public String withdraw(@RequestParam double amount, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            walletService.withdrawFromSavings(userId, amount);
            redirectAttributes.addFlashAttribute("success", "Withdrawn from Savings back to your wallet!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/savings";
    }
}
