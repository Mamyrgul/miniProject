package com.example.miniproject.enity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "message_reactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "message_id"})
) // 🔑 один пользователь = одна реакция на одно сообщение
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messs_gen")
    @SequenceGenerator(sequenceName = "messs_seq", name = "messs_gen", allocationSize = 1, initialValue = 11)
    Long id;

    @Column(nullable = false)
    String emoji; // 😊 👍 ❤️ 😂

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user; // кто поставил смайл

    @ManyToOne
    @JoinColumn(name = "message_id", nullable = false)
    Message message; // к какому сообщению
}
