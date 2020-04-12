package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.Topic;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKey(Topic record);
}