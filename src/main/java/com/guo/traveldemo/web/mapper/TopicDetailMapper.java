package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.TopicDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicDetail record);

    int insertSelective(TopicDetail record);

    TopicDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicDetail record);

    int updateByPrimaryKey(TopicDetail record);
}