package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guo.traveldemo.web.pojo.TravelGroup;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelGroupMapper extends BaseMapper<TravelGroup> {
    int deleteByPrimaryKey(Integer id);

    int insert(TravelGroup record);

    int insertSelective(TravelGroup record);

    TravelGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TravelGroup record);

    int updateByPrimaryKey(TravelGroup record);
}