package com.skillswap.platform.repository;

import com.skillswap.platform.entity.SwapRequest;
import com.skillswap.platform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwapRequestRepository extends JpaRepository<SwapRequest, Long> {
    List<SwapRequest> findBySender(User sender);
    List<SwapRequest> findByReceiver(User receiver);
    List<SwapRequest> findByReceiverAndStatus(User receiver, String status);
    
    // Check if request already exists
    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, String status);

    @org.springframework.data.jpa.repository.Query("SELECT s FROM SwapRequest s WHERE (s.sender = :user OR s.receiver = :user) AND s.status = :status")
    List<SwapRequest> findByUserAndStatus(@org.springframework.data.repository.query.Param("user") User user, @org.springframework.data.repository.query.Param("status") String status);
}
