package com.example.miniproject.repo;

import com.example.miniproject.enity.Item;
import com.example.miniproject.enity.SaveItems;
import com.example.miniproject.enity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SaveItemsRepo extends JpaRepository<SaveItems, Long> {
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
    @Transactional
    @Modifying
    @Query("DELETE FROM SaveItems si WHERE si.item.id = :itemId")
    void deleteByItemId(@Param("itemId") Long itemId);
    Optional<SaveItems> findByUserIdAndItemId(Long userId, Long itemId);
    Optional<SaveItems> findByUserAndItem(User user, Item item);
}
