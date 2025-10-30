package com.example.miniproject.entity;

import com.example.miniproject.enums.MessageStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mes_gen")
    @SequenceGenerator(sequenceName = "mes_seq", name = "mes_gen", allocationSize = 1, initialValue = 11)
    Long id;

    @Column(nullable = false, length = 1000)
    String content;
    String imageUrl;
    LocalDateTime timestamp = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageStatus status = MessageStatus.SENT;
    String linkUrl; // если сообщение — это ссылка

    // Отправитель
    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    // Получатель
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    User receiver;

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MessageReaction> reactions;
}
