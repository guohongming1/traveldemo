package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.StrategyComment;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyComment record);

    int insertSelective(StrategyComment record);

    StrategyComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyComment record);

    int updateByPrimaryKey(StrategyComment record);
}