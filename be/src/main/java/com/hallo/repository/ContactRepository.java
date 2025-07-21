package com.hallo.repository;

import com.hallo.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    // 根据组名查找联系人
    List<Contact> findByGroupName(String groupName);
    
    // 根据名称查找联系人
    List<Contact> findByNameContainingIgnoreCase(String name);
    
    // 根据在线状态查找联系人
    List<Contact> findByOnline(boolean online);
}