package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.web.pojo.StrategyRecomd;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyRecomdMapper extends BaseMapper<StrategyRecomd> {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyRecomd record);

    int insertSelective(StrategyRecomd record);

    StrategyRecomd selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyRecomd record);

    int updateByPrimaryKey(StrategyRecomd record);

    /**
     * 分页搜索查询
     *
     * @param page
     * @return
     */
    IPage<StrategyRecomd> selectPageVo(Page page);
}