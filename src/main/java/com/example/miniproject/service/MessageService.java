package com.example.miniproject.service;

import com.example.miniproject.dto.request.MessageRequest;
import com.example.miniproject.enity.Message;
import com.example.miniproject.enity.MessageReaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    Message sendMessage(MessageRequest request);
    List<Message> getConversation(Long otherUserId);
    void deleteMessage(Long messageId);
    MessageReaction addReaction(Long messageId, String emoji);
    void removeReaction(Long messageId);
}
