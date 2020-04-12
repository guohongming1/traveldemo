package com.guo.traveldemo;

import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.pojo.Strategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TraveldemoApplication.class)
public class TransactionTest {

    @Autowired
    private StrategyMapper strategyMapper;

    @Test
    @Transactional
    public void test(){
        Strategy strategy = new Strategy();
        strategy.setId(2);
        strategy.setTitle("中华大陆");
        strategy.setUserId(23);
        strategyMapper.insert(strategy);
    }
}
