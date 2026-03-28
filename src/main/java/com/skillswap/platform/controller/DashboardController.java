package com.skillswap.platform.controller;

import com.skillswap.platform.entity.Notification;
import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.NotificationRepository;
import com.skillswap.platform.repository.SwapRequestRepository;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            model.addAttribute("user", user);
            
            List<Notification> notifications = notificationRepository.findAllByOrderByCreatedAtDesc();
            model.addAttribute("notifications", notifications);

            long pendingRequestsCount = swapRequestRepository.findByReceiverAndStatus(user, "PENDING").size();
            model.addAttribute("pendingRequestsCount", pendingRequestsCount);
        }
        return "dashboard";
    }
}
