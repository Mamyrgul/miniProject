package com.example.miniproject.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "items")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_gen")
    @SequenceGenerator(sequenceName = "item_seq", name = "item_gen", allocationSize = 1, initialValue = 100)
    Long id;
    String name;
    @Column(length = 500)
    String description;
    boolean isBlocked;
    String address;
    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime createdAt;
    @ElementCollection
    List<String> imageUrls;
    @ManyToOne
    User user;
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    List<Feedback> feedbacks;
}
