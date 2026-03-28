package com.skillswap.platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "swap_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwapRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String requestedSkill;

    @Column(nullable = false)
    private String offeredSkill;

    private String status; // PENDING, ACCEPTED, REJECTED

    private LocalDateTime requestDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }
    
    public User getReceiver() { return receiver; }
    public void setReceiver(User receiver) { this.receiver = receiver; }
    
    public String getRequestedSkill() { return requestedSkill; }
    public void setRequestedSkill(String requestedSkill) { this.requestedSkill = requestedSkill; }
    
    public String getOfferedSkill() { return offeredSkill; }
    public void setOfferedSkill(String offeredSkill) { this.offeredSkill = offeredSkill; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    
    @PrePersist
    public void prePersist() {
        if (requestDate == null) {
            requestDate = LocalDateTime.now();
        }
        if (status == null) {
            status = "PENDING";
        }
    }
}
