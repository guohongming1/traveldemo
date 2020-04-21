package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.web.pojo.Strategy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyMapper extends BaseMapper<Strategy> {
    int deleteByPrimaryKey(Integer id);

    int insert(Strategy record);

    int insertSelective(Strategy record);

    Strategy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Strategy record);

    int updateByPrimaryKey(Strategy record);
    IPage<Strategy> selectPageVo(Page page,@Param("address")String address);
}