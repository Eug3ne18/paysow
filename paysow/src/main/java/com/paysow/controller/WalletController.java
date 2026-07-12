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
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;

    @Autowired
    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping("/cashin")
    public String cashInPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.findById(userId));
        return "cashin";
    }

    @PostMapping("/cashin")
    public String cashIn(@RequestParam double amount, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            walletService.cashIn(userId, amount);
            redirectAttributes.addFlashAttribute("success", "Cash in successful!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cashin";
    }

    @GetMapping("/cashout")
    public String cashOutPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.findById(userId));
        return "cashout";
    }

    @PostMapping("/cashout")
    public String cashOut(@RequestParam String destinationPhone,
                           @RequestParam double amount,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        try {
            walletService.cashOut(userId, destinationPhone, amount);
            redirectAttributes.addFlashAttribute("success", "Cash out to " + destinationPhone + " successful!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/cashout";
    }

    @GetMapping("/history")
    public String history(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        model.addAttribute("user", userService.findById(userId));
        model.addAttribute("transactions", walletService.getHistory(userId));
        return "history";
    }
}
