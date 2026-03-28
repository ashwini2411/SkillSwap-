package com.skillswap.platform.controller;

import com.opencsv.CSVWriter;
import com.skillswap.platform.entity.Notification;
import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.NotificationRepository;
import com.skillswap.platform.repository.SwapRequestRepository;
import com.skillswap.platform.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SwapRequestRepository swapRequestRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        List<User> users = userRepository.findAll().stream()
                .filter(u -> !u.getUsername().equals(principal.getName()))
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        
        // Calculate statistics
        long totalUsers = users.size();
        long totalSkills = users.stream().filter(u -> u.getSkillsOffered() != null && !u.getSkillsOffered().isEmpty()).count();
        long pendingSwaps = swapRequestRepository.findAll().stream().filter(s -> "PENDING".equals(s.getStatus())).count();
        long completedSwaps = swapRequestRepository.findAll().stream().filter(s -> "ACCEPTED".equals(s.getStatus())).count();
        
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalSkills", totalSkills);
        model.addAttribute("pendingSwaps", pendingSwaps);
        model.addAttribute("completedSwaps", completedSwaps);
        
        return "admin-dashboard";
    }

    @PostMapping("/users/{id}/ban")
    public String banUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null && !user.getRole().equals("ROLE_ADMIN")) {
            user.setBanned(true);
            userRepository.save(user);
        }
        return "redirect:/admin/dashboard?success=UserBanned";
    }

    @PostMapping("/users/{id}/unban")
    public String unbanUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setBanned(false);
            userRepository.save(user);
        }
        return "redirect:/admin/dashboard?success=UserUnbanned";
    }

    @PostMapping("/notify")
    public String broadcastNotification(@RequestParam("message") String message) {
        if (message != null && !message.trim().isEmpty()) {
            Notification notification = new Notification();
            notification.setMessage(message.trim());
            notificationRepository.save(notification);
        }
        return "redirect:/admin/dashboard?success=NotificationSent";
    }

    @GetMapping("/users/export-csv")
    public void exportUsersCSV(HttpServletResponse response) throws IOException {
        String filename = "users_export.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        try (CSVWriter writer = new CSVWriter(response.getWriter())) {
            String[] header = {"ID", "Username", "Email", "Role", "Join Date", "Rating", "Status"};
            writer.writeNext(header);

            List<User> users = userRepository.findAll();
            for (User user : users) {
                String[] data = {
                        String.valueOf(user.getId()),
                        user.getUsername(),
                        user.getEmail() != null ? user.getEmail() : "N/A",
                        user.getRole(),
                        user.getJoinDate() != null ? user.getJoinDate().toString() : "N/A",
                        user.getRating() != null ? String.valueOf(user.getRating()) : "N/A",
                        user.isBanned() ? "BANNED" : "ACTIVE"
                };
                writer.writeNext(data);
            }
        }
    }
}
