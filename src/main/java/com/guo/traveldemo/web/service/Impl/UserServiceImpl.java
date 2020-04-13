package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.UserDTO;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageService messageService;

    @Override
    @Transactional
    public Response<Boolean> register(User user, String vercode) {
        // TODO 取得缓存验证码 并进行验证
        if(!vercode.equals("123")){
            return Response.fail(CodeMsg.CODE_TIME_OUT);
        }

        // 查询当前注册用户是否已存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email",user.getEmail());
        User dbEmailUser = userMapper.selectOne(query);
        if(!Objects.isNull(dbEmailUser)){
            return Response.fail(CodeMsg.USER_EXIST_EMAIL);
        }
        // TODO 用户昵称暂时不考虑

        // 用户角色硬编码
        user.setRole("2");
        user.setDate(new Date());

        // 向数据库中新增用户
        if(!(userMapper.insert(user)>0)){
            return Response.fail(CodeMsg.USER_REGISTER_ERROR);
        }


        return Response.success(true);
    }

    /**
     * 用户登录
     * @param user
     * @param session
     * @return
     */
    @Override
    public Response<String> login(UserDTO user, HttpSession session) {
        String email = user.getEmail();
        String password = user.getPassword();
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("email",email);
        query.eq("password",password);
        User userdata = userMapper.selectOne(query);
        if(Objects.isNull(userdata)){
           return Response.fail(CodeMsg.USER_NULL);
        }
        // 查询用户消息
        int msgnum = messageService.getMsgCountByUserId(userdata.getId());
        session.setAttribute("msgnum",msgnum);
        session.setAttribute("userinfo",userdata);
        // 设置session存在最长时间
        session.setMaxInactiveInterval(60 * 60 * 2);
        return Response.success(userdata.getRole());
    }
    public User queryUserById(int id){
        return userMapper.selectByPrimaryKey(id);
    }
}
