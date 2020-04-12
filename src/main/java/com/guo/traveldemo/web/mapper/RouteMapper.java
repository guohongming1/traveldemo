package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.Route;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Route record);

    int insertSelective(Route record);

    Route selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Route record);

    int updateByPrimaryKey(Route record);
}