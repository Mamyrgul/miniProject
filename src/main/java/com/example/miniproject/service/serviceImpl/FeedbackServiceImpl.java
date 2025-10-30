package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.dto.request.FeedbackRequest;
import com.example.miniproject.dto.response.SimpleResponse;
import com.example.miniproject.entity.Feedback;
import com.example.miniproject.entity.Item;
import com.example.miniproject.entity.Like;
import com.example.miniproject.entity.User;
import com.example.miniproject.exception.ForbiddenException;
import com.example.miniproject.exception.NotFoundException;
import com.example.miniproject.repo.FeedbackRepo;
import com.example.miniproject.repo.ItemRepo;
import com.example.miniproject.repo.LikeRepo;
import com.example.miniproject.repo.UserRepository;
import com.example.miniproject.service.FeedbackService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    private final UserRepository userRepo;
    private final ItemRepo itemRepo;
    private final FeedbackRepo feedbackRepo;
    private final LikeRepo likeRepo;

    @Override
    public void saveFeedback(Long itemId, FeedbackRequest feedbackRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new NotFoundException("No such item"));

        Feedback feedback = Feedback.builder()
                .text(feedbackRequest.text())
                .createdAt(feedbackRequest.createdAt() != null ? feedbackRequest.createdAt() : LocalDate.now())
                .user(user)
                .item(item)
                .build();

        if (Boolean.TRUE.equals(feedbackRequest.isLiked())) {
            Like like = Like.builder()
                    .user(user)
                    .feedback(feedback)
                    .build();

            if (feedback.getLikes() == null) {
                feedback.setLikes(new ArrayList<>());
            }
            feedback.getLikes().add(like);
        }

        feedbackRepo.save(feedback);
    }


    @Override
    public SimpleResponse deleteFeedback(Long feedbackId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByEmail(email).orElseThrow(
                ()-> new NotFoundException("User not found"));
        Feedback feedback = feedbackRepo.findById(feedbackId).orElseThrow(
                ()->new NotFoundException("No such feedback"));
        if (!feedback.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not authorized to delete this feedback");
        }
        feedback.setUser(null);
        feedback.setItem(null);
        feedbackRepo.delete(feedback);
        return SimpleResponse
                .builder()
                .message("Feedback deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }
}
