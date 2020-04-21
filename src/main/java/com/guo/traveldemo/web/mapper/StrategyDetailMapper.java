package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyDetail;
import org.apache.ibatis.annotations.Select;
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

    @Select("update strategy_detail set content=#{content} where id = #{id}")
    Integer updateContentById(String content,int id);

}