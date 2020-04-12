package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.Strategy;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Strategy record);

    int insertSelective(Strategy record);

    Strategy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Strategy record);

    int updateByPrimaryKey(Strategy record);
}