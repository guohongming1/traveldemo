package com.guo.traveldemo.web.service.Impl;

import com.guo.traveldemo.web.mapper.UserNotifyMapper;
import com.guo.traveldemo.web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/13
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private UserNotifyMapper userNotifyMapper;
    /**
     *根据用户ID获取消息
     * @param userid
     * @return
     */
    @Override
    public int getMsgCountByUserId(int userid) {
        return userNotifyMapper.getMsgCountByUserId(userid);
    }
}
