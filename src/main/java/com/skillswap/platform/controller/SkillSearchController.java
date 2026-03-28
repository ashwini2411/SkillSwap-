package com.skillswap.platform.controller;

import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SkillSearchController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/skills/search")
    public String searchSkills(@RequestParam(value = "q", required = false) String query,
                               Model model, Principal principal) {
        
        if (principal == null) return "redirect:/login";
        
        String currentUsername = principal.getName();
        List<User> results = null;

        if (query != null && !query.trim().isEmpty()) {
            results = userRepository.findBySkillsOfferedContainingIgnoreCase(query.trim());
            
            // Remove the currently logged-in user from the results (can't swap with yourself)
            results = results.stream()
                    .filter(u -> !u.getUsername().equals(currentUsername))
                    .collect(Collectors.toList());
            
            model.addAttribute("query", query);
        } else {
            // Show all users who have offered skills, excluding self
            List<User> allUsers = userRepository.findAll();
            results = allUsers.stream()
                    .filter(u -> !u.getUsername().equals(currentUsername) && 
                                  u.getSkillsOffered() != null && 
                                  !u.getSkillsOffered().isEmpty())
                    .collect(Collectors.toList());
        }

        model.addAttribute("users", results);
        return "search";
    }
}
