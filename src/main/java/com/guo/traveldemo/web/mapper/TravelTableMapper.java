package com.guo.traveldemo.web.mapper;

import com.guo.traveldemo.web.pojo.TravelTable;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelTableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TravelTable record);

    int insertSelective(TravelTable record);

    TravelTable selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TravelTable record);

    int updateByPrimaryKey(TravelTable record);
}