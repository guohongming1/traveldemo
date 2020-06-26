package com.guo.traveldemo;

import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.util.RedisUtil;
import com.guo.traveldemo.web.service.Impl.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrimaryApplicationTests {

  @Autowired private RedisUtil redisUtil;
  @Autowired
  private RedisService redisService;


  @Test
  public void contextLoads() throws Exception{

//   for(int i=0;i<30;i++){
//     List<Integer> listId = redisService.getTopNum(Constants.QUESTION_HOT_NAME);
//     System.out.println(listId);
//     Thread.sleep(1000);
//     System.out.println(redisService.getTopNum(Constants.TOPIC_HOT_NAME));
//     System.out.println(redisService.getTopNum(Constants.ESSAY_HOT_NAME));
//   }

//    redisService.delHot(2,Constants.USER_QUESTION_COM_NUM);
//    List<Integer> listId = redisService.getTopNum(Constants.USER_QUESTION_COM_NUM);
//    if(listId != null){
//      listId.forEach(item-> System.out.println(item));
//    }else{
//      System.out.println("shujuweiNull");
//    }
//    redisUtil.set(ViewKey.ESSAY_KEY, "article-id:1:num", 1);
//    redisUtil.increment(ViewKey.ESSAY_KEY, "article-id:1:num");
//    System.out.println(java.util.Optional.ofNullable(redisUtil.get(ViewKey.ESSAY_KEY, "article-id:1:num")));
  }
}
