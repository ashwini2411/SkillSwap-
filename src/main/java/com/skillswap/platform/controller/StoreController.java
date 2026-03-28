package com.skillswap.platform.controller;

import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class StoreController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/store")
    public String viewStore(Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        User currentUser = userRepository.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("user", currentUser);

        // We can just hardcode the store catalog in the view for simplicity!
        return "store";
    }

    @PostMapping("/store/buy")
    public String purchaseItem(@RequestParam("itemId") String itemId,
                               @RequestParam("cost") Integer cost,
                               @RequestParam("badgeName") String badgeName,
                               Principal principal) {
        if (principal == null) return "redirect:/login";
        
        User user = userRepository.findByUsername(principal.getName()).orElse(null);
        if (user != null) {
            Integer currentPoints = user.getPoints();
            if (currentPoints == null) currentPoints = 0;
            
            if (currentPoints >= cost) {
                user.setPoints(currentPoints - cost);
                user.setBadge(badgeName);
                userRepository.save(user);
                return "redirect:/store?success=PurchaseComplete";
            } else {
                return "redirect:/store?error=InsufficientPoints";
            }
        }
        return "redirect:/store?error=UnknownError";
    }
}
