package com.hallo.repository;

import com.hallo.model.Contact;
import com.hallo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // 查找两个联系人之间的所有消息
    List<Message> findBySenderAndReceiverOrderByTimestampDesc(Contact sender, Contact receiver);
    
    // 查找发送给特定联系人的所有消息
    List<Message> findByReceiverOrderByTimestampDesc(Contact receiver);
    
    // 查找特定联系人发送的所有消息
    List<Message> findBySenderOrderByTimestampDesc(Contact sender);
    
    // 查找两个联系人之间的所有消息（双向）
    List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            Contact sender1, Contact receiver1, Contact sender2, Contact receiver2);
    
    // 查找未读消息
    List<Message> findByReceiverAndIsReadFalseOrderByTimestampAsc(Contact receiver);
}