package com.paysow.controller;

import com.paysow.model.User;
import com.paysow.service.UserService;
import com.paysow.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public DashboardController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        User user = userService.findById(userId);

        model.addAttribute("user", user);
        model.addAttribute("recentTransactions", walletService.getRecentHistory(userId));
        return "dashboard";
    }
}
