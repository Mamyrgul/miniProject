package com.example.miniproject.controller;

import com.example.miniproject.dto.request.MessageRequest;
import com.example.miniproject.dto.response.MessageResponse;
import com.example.miniproject.entity.Message;
import com.example.miniproject.entity.MessageReaction;
import com.example.miniproject.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600, origins = "*")
public class MessageApi {

    private final MessageService messageService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody MessageRequest request) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/chat/{userId2}")
    public List<Message> getChat(@PathVariable Long userId2) {
        return messageService.getConversation(userId2);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteMessage(@PathVariable Long userId) {
        messageService.deleteMessage(userId);
    }

    @PostMapping("/{messageId}/react")
    public MessageReaction react(@PathVariable Long messageId, @RequestParam String emoji
    ) {
        return messageService.addReaction(messageId, emoji);
    }

    @DeleteMapping("/{messageId}/react")
    public void removeReaction(@PathVariable Long messageId) {
        messageService.removeReaction(messageId);
    }
    @GetMapping("/chat/{otherUserId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MessageResponse>> getFullChatWithUser(@PathVariable Long otherUserId) {
        List<MessageResponse> chat = messageService.getFullChatWithUser(otherUserId);
        return ResponseEntity.ok(chat);
    }
}
