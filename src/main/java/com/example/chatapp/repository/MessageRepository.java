package com.example.chatapp.repository;

import com.example.chatapp.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    // Find last 50 messages to show when someone joins the chat
    List<MessageEntity> findTop50ByOrderByTimestampAsc();
}