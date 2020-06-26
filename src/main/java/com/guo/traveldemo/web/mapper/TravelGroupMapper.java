package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.web.pojo.TravelGroup;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelGroupMapper extends BaseMapper<TravelGroup> {
    int deleteByPrimaryKey(Integer id);

    int insert(TravelGroup record);

    int insertSelective(TravelGroup record);

    TravelGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TravelGroup record);

    int updateByPrimaryKey(TravelGroup record);

    IPage<TravelGroup> selectPageVo(Page page, @Param("flag")Byte flag);

}