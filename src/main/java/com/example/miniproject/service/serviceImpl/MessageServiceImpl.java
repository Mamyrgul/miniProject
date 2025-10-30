package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.request.MessageRequest;
import com.example.miniproject.dto.response.MessageResponse;
import com.example.miniproject.entity.Message;
import com.example.miniproject.entity.MessageReaction;
import com.example.miniproject.entity.User;
import com.example.miniproject.enums.MessageStatus;
import com.example.miniproject.repo.MessageReactionRepo;
import com.example.miniproject.repo.MessageRepo;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.service.MessageService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepo messageRepository;
    private final UserRepository userRepository;
    private final MessageReactionRepo reactionRepository;
    @Transactional
    public Message sendMessage(MessageRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .content(request.getContent())
                .imageUrl(request.getImageUrl())
                .linkUrl(request.getLinkUrl())
                .status(MessageStatus.SENT)
                .sender(sender)
                .receiver(receiver)
                .build();

        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long otherUserId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        User other = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        return messageRepository
                .findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(currentUser, other, currentUser, other);
    }

    /** Удаление своего сообщения */
    @Transactional
    public void deleteMessage(Long messageId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Message msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!msg.getSender().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can delete only your own messages");
        }

        messageRepository.delete(msg);
    }

    /** Добавить реакцию */
    public MessageReaction addReaction(Long messageId, String emoji) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        MessageReaction reaction = MessageReaction.builder()
                .emoji(emoji)
                .user(user)
                .message(message)
                .build();

        return reactionRepository.save(reaction);
    }

    /** Удалить реакцию */
    public void removeReaction(Long messageId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        reactionRepository.deleteByUserIdAndMessageId(user.getId(), messageId);
    }

    public List<MessageResponse> getFullChatWithUser(Long otherUserId) {
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));
        List<Message> messages = messageRepository.findFullConversation(currentUser, otherUser);
        return messages.stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getContent(),
                        m.getImageUrl(),
                        m.getLinkUrl(),
                        m.getSender().getUsername(),      // имя отправителя
                        m.getReceiver().getUsername(),    // имя получателя
                        m.getTimestamp(),
                        m.getStatus().name()          // статус (SENT, READ и т.п.)
                ))
                .toList();
    }
}