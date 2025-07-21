package com.hallo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String avatar;
    private String groupName;
    private boolean online;
    private String lastMessage;
    private String lastMessageTime;
    
    // 构造函数，不包含 id
    public Contact(String name, String avatar, String groupName, boolean online, String lastMessage, String lastMessageTime) {
        this.name = name;
        this.avatar = avatar;
        this.groupName = groupName;
        this.online = online;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}