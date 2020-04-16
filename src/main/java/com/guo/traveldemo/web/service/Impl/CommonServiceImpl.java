package com.guo.traveldemo.web.service.Impl;

import com.guo.traveldemo.web.mapper.QuestionMapper;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/14
 */
@Service
public class CommonServiceImpl{

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private QuestionMapper questionMapper;

    // 攻略id获取用户ID
    int getUserIdByStrategyID(int id){
        return strategyMapper.selectByPrimaryKey(id).getUserId();
    }
}
