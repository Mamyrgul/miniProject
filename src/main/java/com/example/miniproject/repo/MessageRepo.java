package com.example.miniproject.repo;

import com.example.miniproject.enity.Message;
import com.example.miniproject.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            User sender, User receiver, User receiver2, User sender2
    );
    void delete(Message msg);
}
