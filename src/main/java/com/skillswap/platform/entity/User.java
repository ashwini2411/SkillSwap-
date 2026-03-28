package com.skillswap.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    private String password; // Will be hashed via BCrypt

    private String role; // e.g., "ROLE_USER" or "ROLE_ADMIN"

    // Profile Management
    @Column(columnDefinition = "TEXT")
    private String skillsOffered;

    @Column(columnDefinition = "TEXT")
    private String skillsWanted;

    private boolean isBanned;

    private Double rating;

    private Integer points = 0;

    private String badge;

    @Column(name = "join_date", updatable = false)
    private LocalDateTime joinDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getSkillsOffered() { return skillsOffered; }
    public void setSkillsOffered(String skillsOffered) { this.skillsOffered = skillsOffered; }
    
    public String getSkillsWanted() { return skillsWanted; }
    public void setSkillsWanted(String skillsWanted) { this.skillsWanted = skillsWanted; }
    
    public boolean isBanned() { return isBanned; }
    public void setBanned(boolean isBanned) { this.isBanned = isBanned; }
    
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
    
    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }
    
    public LocalDateTime getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDateTime joinDate) { this.joinDate = joinDate; }

    @PrePersist
    public void prePersist() {
        if (joinDate == null) {
            joinDate = LocalDateTime.now();
        }
    }

    // Spring Security UserDetails Methods
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBanned; // Custom Ban Logic implementation
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
