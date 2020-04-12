package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.GroupType;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GroupType record);

    int insertSelective(GroupType record);

    GroupType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GroupType record);

    int updateByPrimaryKey(GroupType record);
}