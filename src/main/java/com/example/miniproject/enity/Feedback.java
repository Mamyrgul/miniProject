package com.example.miniproject.enity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "feedbacks")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "feedBack_gen")
    @SequenceGenerator(sequenceName = "feedBack_seq", name = "feedBack_gen", allocationSize = 1, initialValue = 11)
    Long id;
    @Column(length = 500)
    String text;
    Integer rating;
    LocalDate createdAt;
    @ElementCollection
    List<String> imageUrls;
    @ManyToOne
    User user;
    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL)
    List<Like> likes;
    @ManyToOne
    Item item;
}
