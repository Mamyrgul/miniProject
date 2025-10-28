package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.service.MessageReactionService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Builder
@Transactional
public class MessageReactionServiceImpl implements MessageReactionService {
}
