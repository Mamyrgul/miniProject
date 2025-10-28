package com.example.miniproject.enity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "savedItems")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "save_gen")
    @SequenceGenerator(sequenceName = "save_seq", name = "save_gen", allocationSize = 1, initialValue = 11)
    Long id;
    LocalDate addedAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
