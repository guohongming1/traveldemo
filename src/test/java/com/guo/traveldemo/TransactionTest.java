package com.guo.traveldemo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.cache.TestKey;
import com.guo.traveldemo.util.RedisUtil;
import com.guo.traveldemo.web.mapper.RoleMapper;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.Role;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.User;
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
    private RoleMapper roleMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test(){
//        Role role = new Role();
//        role.setId(8);
//        role.setName("测试2");
//        roleMapper.insert(role);
//        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id",1);
//        Role role1 = roleMapper.selectOne(queryWrapper);
//        System.out.println(role1);
        User user = new User();
        user.setName("测试数据");
        userMapper.insert(user);
    }

    @Test
    public void redisTest(){
        Role role = new Role();
        role.setId(1);
        role.setName("张三");
        // "TestKey:prefix:test"
        redisUtil.set(TestKey.TEST, "test", role);
        System.out.println(java.util.Optional.ofNullable(redisUtil.get(TestKey.TEST, "test")));
    }
}
