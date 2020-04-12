package com.guo.traveldemo.web.service.Impl;

import com.guo.traveldemo.web.mapper.StrategyDetailMapper;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyDetail;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Service
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private StrategyDetailMapper strategyDetailMapper;
    /*
      存储游记
     */
    @Override
    @Transactional
    public Boolean save(Strategy strategy,StrategyDetail strategyDetail){
        if(strategyMapper.insert(strategy) > 0 && strategyDetailMapper.insert(strategyDetail) > 0){
            return true;
        }
        return false;
    }

}
