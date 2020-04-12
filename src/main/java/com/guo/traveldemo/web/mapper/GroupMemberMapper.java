package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.GroupMember;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupMember record);

    int insertSelective(GroupMember record);

    GroupMember selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupMember record);

    int updateByPrimaryKey(GroupMember record);
}