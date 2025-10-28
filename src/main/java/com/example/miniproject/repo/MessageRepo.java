package com.example.miniproject.repo;

import com.example.miniproject.enity.Message;
import com.example.miniproject.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            User sender, User receiver, User receiver2, User sender2
    );
    void delete(Message msg);
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.sender s
        JOIN FETCH m.receiver r
        WHERE (s = :currentUser AND r = :otherUser)
           OR (s = :otherUser AND r = :currentUser)
        ORDER BY m.timestamp ASC
    """)
    List<Message> findFullConversation(
            @Param("currentUser") User currentUser,
            @Param("otherUser") User otherUser
    );
}
