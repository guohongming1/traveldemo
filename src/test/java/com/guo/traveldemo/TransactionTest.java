package com.guo.traveldemo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.cache.TestKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.util.IPUtils;
import com.guo.traveldemo.util.RedisUtil;
import com.guo.traveldemo.util.ScenicApiUtil;
import com.guo.traveldemo.web.mapper.RoleMapper;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.mapper.StrategyRecomdMapper;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.Role;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyRecomd;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.RecommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

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
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private StrategyRecomdMapper strategyRecomdMapper;

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private RecommentService recommentService;

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
//        User user = new User();
//        user.setId(5);
//        user.setName("测试数据");
//        user.setDate(new Date());
//        userMapper.insert(user);
        //messageService.queryUserREJECTMsg(4);
        System.out.println(userMapper.selectByPrimaryKey(5).getDate());
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

    @Test
    public void IPTest()throws Exception{
        IPUtils.getAddresses("ip=218.192.3.42&json=true","GBK");
    }

    @Test
    public void scenicURL() throws Exception{  //30a4a34aaafc4b2d8453b8978f9acf5a

        //http://api.map.baidu.com/place/v2/search?query=%E7%BF%A0%E5%BE%AE%E5%B3%B0&tag=%E6%97%85%E6%B8%B8%E6%99%AF%E7%82%B9&region=%E5%85%A8%E5%9B%BD&output=json&ak=w5zGad9BhqDpzFE6sDQarpSiY1TzhTzt
         ScenicApiUtil.getScenicInfoByURL("翠微峰","全国");
//        String imei= (String) jsonObject.get("showapi_res_id");
//        System.out.println(imei);


        String result = "\n" +
                "{\n" +
                " \n" +
                "    \"success\":\"true\",\n" +
                " \n" +
                "    \"data\":[{\n" +
                " \n" +
                "        \"shop_uid\":\"123\"\n" +
                " \n" +
                "    },\n" +
                " \n" +
                "    {\n" +
                " \n" +
                "        \"shop_name\":\"张三\"\n" +
                " \n" +
                "    }]}\n";

        JSONObject json = JSONObject.parseObject(result);
        //获取item，得到json数组
        JSONArray array = json.getJSONArray("data");
        JSONObject jo = array.getJSONObject(0);
        jo.getString("shop_uid");
        System.out.println(jo);

    }

    @Test
    public void copyTravelStrategy(){
        QueryWrapper<Strategy> query = new QueryWrapper<>();
        query.in("id",8,9,10,11,12);
        List<Strategy> list = strategyMapper.selectList(query);
        for(Strategy s:list){
            StrategyRecomd recomd = new StrategyRecomd();
            BeanUtils.copyProperties(s,recomd);
            strategyRecomdMapper.insertSelective(recomd);
        }
    }

    @Test
    public void PageTest(){
         List<StrategyRecomd> list = recommentService.getList(2,1);
         for(StrategyRecomd s:list){
             System.out.println(s.getId());
         }
    }

    @Test
    public void getHotTopTest(){
//        redisService.addHot(1,"2", Constants.ESSAY_HOT_NAME);
//        redisService.addHot(2,"1", Constants.ESSAY_HOT_NAME);
//        redisService.addHot(3,"3", Constants.ESSAY_HOT_NAME);
        redisService.addHot(10,"1", Constants.ESSAY_HOT_NAME);
        Set<String> set = redisUtil.sortSetRangeAndScore(Constants.ESSAY_HOT_NAME,0,5);
        System.out.println(redisService.getScore(Constants.ESSAY_HOT_NAME, 10));
        System.out.println("test");
//        for (String str : set) {
////            System.out.println(str);
////        }
////        System.out.println(set.toArray());
//        System.out.println(redisUtil.sortSetAdd("test","art",1));
//        System.out.println(redisUtil.getHotScore(null,"art","test"));
//        System.out.println(redisUtil.sortSetZincrby("test",null,"art",3));
//        System.out.println(redisUtil.getHotScore(null,"art","test"));
        //System.out.println(redisUtil.sortSetAdd("test:zSet","keyword",30));
       // System.out.println(redisUtil.getHotScore(null,"keyword","test:zSet"));
        //System.out.println(redisUtil.sortSetZincrby("test:zSet",null,"keyword",3));
    }
}
