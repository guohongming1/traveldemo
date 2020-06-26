package com.guo.traveldemo.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.common.exception.GlobalException;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.dto.UserDTO;
import com.guo.traveldemo.web.mapper.CollectMapper;
import com.guo.traveldemo.web.pojo.Collect;
import com.guo.traveldemo.web.pojo.Notify;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.Impl.CommonServiceImpl;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.StrategyService;
import com.guo.traveldemo.web.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CommonServiceImpl commonService;
    @Autowired
    private CollectMapper collectMapper;

    /**
     * 用户注册
     * @param user
     * @param vercode
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Response<Boolean> register(User user, String vercode){
        if(user == null){
            return Response.fail(CodeMsg.USER_NULL);
        }
       return  userService.register(user, vercode);
    }

    /**
     * 发送邮箱随机数
     *
     * @return
     */
    @PostMapping("email-send")
    @ResponseBody
    public Response<Boolean> emailSend(String email, String mode) {
        // 邮箱为空
        if (StringUtils.isEmpty(email)) {
            return Response.fail(CodeMsg.MAIL_NULL);
        }

        return userService.sendVercode(email, mode);
    }

    /*
     用户登录
     */
    @PostMapping("/login")
    @ResponseBody
    public Response<String> login(UserDTO user, HttpSession session){
        if(user == null){
            return Response.fail(CodeMsg.USER_NULL);
        }
        return userService.login(user,session);

    }

    /**
     * 用户登出
     *
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public Response<Boolean> logout(HttpSession session) {
        if (session != null) {
            session.removeAttribute("userinfo");
            session.removeAttribute("msgnum");
            session.invalidate();
        }
        return Response.success(true);
    }

    /**
     * 修改信息
     * @param userdto
     * @return
     */
    @PostMapping("/reuserinfo")
    @ResponseBody
    public Response<String> reuserinfo(HttpSession session,User userdto){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            if(userdto.getRemark()!= null){
                user.setRemark(userdto.getRemark());
            }
            if(userdto.getName() != null){
                user.setName(userdto.getName());
            }
            if(userdto.getTitle() !=null){
                user.setTitle(userdto.getTitle());
            }
            if(userdto.getSex() != null){
                user.setSex(userdto.getSex());
            }
            if(userService.reuserinfo(user)>0){
                // 更改session状态
                session.setAttribute("userinfo",user);
                return Response.success("成功");
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 修改密码
     * @param session
     * @param nowpass
     * @param pass
     * @return
     */
    @PostMapping("/repass")
    @ResponseBody
    public Response<String> repass(HttpSession session, String nowpass,String pass){
       User user = (User)session.getAttribute("userinfo");
       if(user.getPassword().equals(nowpass)){
          user.setPassword(pass);
           if(userService.reuserinfo(user)>0){
               // 清除session 重新登录
               session.removeAttribute("userinfo");
               session.removeAttribute("msgnum");
               session.invalidate();
           }
           return Response.success("成功");
       }else{
           return Response.success("旧密码错误");
       }
    }

    /**
     * 忘记密码
     */
    @PostMapping("/forgetpass")
    @ResponseBody
    public Response<String> forgetpass(User user,String vercode){
        if (StringUtils.isEmpty(user.getEmail()) || StringUtils.isEmpty(user.getPassword())) {
            return Response.fail(CodeMsg.FAIL);
        }
       return userService.forgetpass(user,vercode);
    }
    /**
     * 修改头像
     * @param session
     * @param userHeadImgSrc
     * @return
     */
    @PostMapping("/reheadimg")
    @ResponseBody
    public Response<String> reheadimg(HttpSession session, String userHeadImgSrc){
        User user = (User)session.getAttribute("userinfo");
        user.setImgUrl(userHeadImgSrc);
        if(userService.reuserinfo(user)>0){
            // 更改session状态
            session.setAttribute("userinfo",user);
        }
        return Response.success("成功");
    }
    /**
     * 拉取用户未读消息
     */
    @PostMapping("/userReMsg")
    @ResponseBody
    public Response<List<Notify>> queryMsg(HttpSession session){
        List<Notify> list = new ArrayList<Notify>();
        User user = (User)session.getAttribute("userinfo");
        if(null != user && user.getId() !=null){
            list = messageService.queryUserREJECTMsg(user.getId());
        }
        return Response.success(list);
    }

    /**
     * 删除单条信息
     * @param session
     * @param msgId
     * @return
     */
    @PostMapping("/delOneMsg")
    @ResponseBody
    public Response<String> delOneMsgById(HttpSession session,int msgId){
        User user = (User)session.getAttribute("userinfo");
        if(user != null && !Objects.isNull(msgId)){
            if(messageService.delOneMsgById(user.getId(),msgId)>0){
                return Response.success("成功");
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 删除全部信息
     * @param session
     * @return
     */
    @PostMapping("/delAllMsg")
    @ResponseBody
    public Response<String> delAllMsg(HttpSession session){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            if(messageService.delBatchMsgById(user.getId())>0){
                return Response.success("成功");
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 发送私信
     * @param session
     * @param acpter
     * @param content
     * @return
     */
    @PostMapping("/sendpermsg")
    @ResponseBody
    public Response<String> sendPerMsg(HttpSession session,int acpter,String content){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            // 组装消息
            String msg = "<a href='/front/userInfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>"+"向您发送了一条信息:"+content;
           return messageService.sendMsg(user.getId(),acpter,msg);
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 个人攻略 收藏
     * @param session
     * @return
     */
    @PostMapping("/getPerstrategy")
    @ResponseBody
    public Response<Map<String,Object>> getPerstrategy(HttpSession session){
        User sessionuser = (User)session.getAttribute("userinfo");
        List<Strategy> list = strategyService.getStrategyByUserId(sessionuser.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("strategy",this.queryList(list));
        // 查询收藏
        List<Collect> collectList = commonService.getCollectList(sessionuser.getId(),(byte)1);
        List<Strategy> collectStraList = new ArrayList<>();
        if(collectList != null){
            collectList.forEach(item->{
                collectStraList.add(strategyService.selectStrategyById(item.getProId()));
            });
        }
        map.put("colstrategy",this.queryList(collectStraList));
        return Response.success(map);
    }

    /**
     * 删除攻略  非物理删除
     * @param session
     * @param id
     * @return
     */
    @PostMapping("/delOneStrategy")
    @ResponseBody
    public Response<String> delOneStrategy(HttpSession session,int id){
        User sessionuser = (User)session.getAttribute("userinfo");
        QueryWrapper<Strategy> query = new QueryWrapper<>();
        query.eq("user_id",sessionuser.getId());
        query.eq("id",id);
        query.eq("del_flag",(byte)0);
        List<Strategy> list = strategyService.queryStrategy(query);
        if(list != null && list.size() >0){
             Strategy strategy = new Strategy();
             strategy.setId(list.get(0).getId());
             strategy.setDelFlag((byte)1);
             if(redisService.delHot(id,Constants.ESSAY_HOT_NAME)){
                 if(strategyService.updateStrategy(strategy)>0){
                     // 删除热度
                     redisService.delHot(strategy.getId(),Constants.ESSAY_HOT_NAME);
                     return Response.success("成功");
                 }
             }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 删除攻略收藏 物理删除
     * @param session
     * @param id
     * @return
     */
    @PostMapping("/delstraCol")
    @ResponseBody
     public Response<String> delstraCol(HttpSession session,int id){
        User sessionuser = (User)session.getAttribute("userinfo");
        if(sessionuser != null){
            if(collectMapper.deleteByPrimaryKey(id)>0){
                return Response.success("成功");
            }
        }
        return Response.fail(CodeMsg.FAIL);
     }
    /**
     * 封装攻略DTO
     * @param list
     * @return
     */
    public List<StrategyDTO> queryList(List<Strategy> list){
        List<StrategyDTO> result = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            StrategyDTO strategyDTO = new StrategyDTO();
            BeanUtils.copyProperties(list.get(i),strategyDTO);
            User user = userService.queryUserById(list.get(i).getUserId());
            strategyDTO.setUserHeadImg(user.getImgUrl());
            strategyDTO.setUserName(user.getName());
            Number hotNum = redisService.getScore(Constants.ESSAY_HOT_NAME, list.get(i).getId());
            if(!Objects.isNull(hotNum)){
                strategyDTO.setViewNum(hotNum.intValue());
            }else{
                strategyDTO.setViewNum(0);
            }
            Number colNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_COL_NUM);
            if(!Objects.isNull(colNum)){
                strategyDTO.setCollectnum(colNum.intValue());
            }else{
                strategyDTO.setCollectnum(0);
            }
            Number comNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_COM_NUM);
            if(!Objects.isNull(comNum)){
                strategyDTO.setCommentnum(comNum.intValue());
            }else{
                strategyDTO.setCommentnum(0);
            }
            result.add(strategyDTO);
        }
        return result;
    }

}
