package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.request.MessageRequest;
import com.example.miniproject.dto.response.MessageResponse;
import com.example.miniproject.enity.Message;
import com.example.miniproject.enity.MessageReaction;
import com.example.miniproject.enity.User;
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
        // ✅ 1. Получаем email текущего пользователя
        String currentUserEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        // ✅ 2. Проверяем, что пользователь существует
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        // ✅ 3. Проверяем, что собеседник существует
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // ✅ 4. Получаем чат через JOIN FETCH (один SQL-запрос)
        List<Message> messages = messageRepository.findFullConversation(currentUser, otherUser);

        // ✅ 5. Маппим в DTO
        return messages.stream()
                .map(m -> new MessageResponse(
                        m.getId(),
                        m.getContent(),
                        m.getImageUrl(),
                        m.getLinkUrl(),
                        m.getSender().getName(),      // имя отправителя
                        m.getReceiver().getName(),    // имя получателя
                        m.getTimestamp(),
                        m.getStatus().name()          // статус (SENT, READ и т.п.)
                ))
                .toList();
    }
}