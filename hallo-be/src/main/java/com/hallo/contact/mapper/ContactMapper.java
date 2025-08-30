package com.hallo.contact.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hallo.contact.mapper.model.ContactModel;
import com.hallo.contact.mapper.model.GroupModel;
import com.hallo.contact.mapper.parameter.ContactParameter;
import com.hallo.contact.mapper.parameter.GroupParameter;

/**
 * 
 * @description description
 * @author Administrator
 * @date currentDate
 */
@Mapper
public interface ContactMapper {
    List<ContactModel> getFriendList(ContactParameter parameter);

    List<GroupModel> getGroupList(GroupParameter gp);
}
