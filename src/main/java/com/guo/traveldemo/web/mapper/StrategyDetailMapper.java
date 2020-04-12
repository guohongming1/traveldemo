package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.StrategyDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyDetail record);

    int insertSelective(StrategyDetail record);

    StrategyDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyDetail record);

    int updateByPrimaryKeyWithBLOBs(StrategyDetail record);

    int updateByPrimaryKey(StrategyDetail record);
}