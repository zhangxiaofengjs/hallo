package com.hallo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Contact sender;
    
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Contact receiver;
    
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
    
    // 构造函数，不包含 id
    public Message(Contact sender, Contact receiver, String content, LocalDateTime timestamp, boolean isRead) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }
}