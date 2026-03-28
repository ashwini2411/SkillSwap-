package com.skillswap.platform.controller;

import com.skillswap.platform.entity.User;
import com.skillswap.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin-login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password) {
        // Simple validation checks (ideally handled via validation framework)
        if (userRepository.findByUsername(username).isPresent()) {
            return "redirect:/signup?error=UsernameExists";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return "redirect:/signup?error=EmailExists";
        }

        // Create new user entity
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        
        if ("admin".equalsIgnoreCase(username)) {
            newUser.setRole("ROLE_ADMIN");
        } else {
            newUser.setRole("ROLE_USER");
        }
        
        newUser.setBanned(false);
        userRepository.save(newUser);

        return "redirect:/login?success=AccountCreated";
    }
}
