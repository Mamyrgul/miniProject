package com.example.miniproject.service;

import com.example.miniproject.dto.request.MessageRequest;
import com.example.miniproject.dto.response.MessageResponse;
import com.example.miniproject.entity.Message;
import com.example.miniproject.entity.MessageReaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    Message sendMessage(MessageRequest request);
    List<Message> getConversation(Long otherUserId);
    void deleteMessage(Long messageId);
    MessageReaction addReaction(Long messageId, String emoji);
    void removeReaction(Long messageId);
    List<MessageResponse> getFullChatWithUser(Long otherUserId);
}
