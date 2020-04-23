package com.guo.traveldemo.web.service;

import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.UserDTO;
import com.guo.traveldemo.web.pojo.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.http.HttpSession;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
public interface UserService {
    Response<Boolean> register(User user,String vercode);

    Response<String> login(UserDTO userDTO, HttpSession session);

    User queryUserById(int id);
    int reuserinfo(User user);
}
