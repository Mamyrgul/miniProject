package com.example.miniproject.repo;

import com.example.miniproject.enity.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReactionRepo extends JpaRepository<MessageReaction,Long> {
    void deleteByUserIdAndMessageId(Long userId, Long userId1);
}
