package com.guo.traveldemo.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.pojo.Notify;
import com.guo.traveldemo.web.pojo.UserNotify;

import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/13
 */
public interface MessageService {
     int getMsgCountByUserId(int id);
     void sendRemind(int target, int targetType ,int action, int sender, String content);
     Response<String> sendMsg(int sender, int acpter, String content);
     List<Notify> queryUserAcptMsg(int userId);
     List<Notify> queryUserREJECTMsg(int userId);
     int delOneMsgById(int userId,int msgId);
     int delBatchMsgById(int userId);
     List<UserNotify> queryUserNotify(QueryWrapper<UserNotify> query);
     int delNotifyById(int id);
     int delBatchUserNotify(List<Integer> ids);
}
