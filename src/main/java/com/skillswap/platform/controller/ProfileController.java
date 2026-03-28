package com.skillswap.platform.controller;

import com.skillswap.platform.entity.Review;
import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.ReviewRepository;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/profile")
    public String viewProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            List<Review> reviews = reviewRepository.findByRevieweeOrderByCreatedAtDesc(user);
            model.addAttribute("reviews", reviews);
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/{id}")
    public String viewPublicProfile(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return "redirect:/dashboard?error=UserNotFound";
        
        List<Review> reviews = reviewRepository.findByRevieweeOrderByCreatedAtDesc(user);
        model.addAttribute("targetUser", user);
        model.addAttribute("reviews", reviews);
        
        // Let's pass the currently logged-in user too to show the username in navbar
        model.addAttribute("user", userRepository.findByUsername(principal.getName()).orElse(null));
        return "public-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String skillsOffered,
                                @RequestParam String skillsWanted,
                                Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        user.setSkillsOffered(skillsOffered);
        user.setSkillsWanted(skillsWanted);
        userRepository.save(user);

        return "redirect:/profile?success";
    }
}
