package com.skillswap.platform.repository;

import com.skillswap.platform.entity.Message;
import com.skillswap.platform.entity.SwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySwapRequestOrderBySentAtAsc(SwapRequest swapRequest);
}
