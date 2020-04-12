package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.Group;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
}