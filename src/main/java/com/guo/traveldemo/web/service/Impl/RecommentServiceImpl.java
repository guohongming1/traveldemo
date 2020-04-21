package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.HotMap;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.mapper.StrategyRecomdMapper;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyRecomd;
import com.guo.traveldemo.web.service.RecommentService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/20
 */
@Service
public class RecommentServiceImpl implements RecommentService {
    @Autowired
    private StrategyRecomdMapper strategyRecomdMapper;

    @Autowired
    private StrategyMapper strategyMapper;


    @Override
    public List<StrategyRecomd> getList(int limit, int page) {
        // 分页
        Page<StrategyRecomd> pageHelper = new Page<>();
        pageHelper.setSize(limit);
        pageHelper.setCurrent(page);

        IPage<StrategyRecomd> pageVo = null;

        pageVo = strategyRecomdMapper.selectPageVo(pageHelper);
        return pageVo.getRecords();
    }

    @Override
    public List<StrategyDTO> top(List<Integer> recommends, boolean topTen){
        if (recommends == null || recommends.size() == 0) {
            return null;
        }
        List<Strategy> list = new ArrayList<>();
        // 按照排名先后查询
        recommends.forEach(id->list.add(strategyMapper.selectById(id)));
        return null;
    }
}
