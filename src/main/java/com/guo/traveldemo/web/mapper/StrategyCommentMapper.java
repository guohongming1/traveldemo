package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.web.pojo.StrategyComment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyCommentMapper extends BaseMapper<StrategyComment> {
    int deleteByPrimaryKey(Integer id);

    int insert(StrategyComment record);

    int insertSelective(StrategyComment record);

    StrategyComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StrategyComment record);

    int updateByPrimaryKey(StrategyComment record);

    IPage<StrategyComment> selectPageVo(Page page,@Param("detailId") int detailId);

    int delCommentByStrategyId(int id);
}