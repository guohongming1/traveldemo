package com.guo.traveldemo.web.service;

import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyDetail;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
public interface StrategyService {

    public Boolean save(Strategy strategy, StrategyDetail strategyDetail);
}
