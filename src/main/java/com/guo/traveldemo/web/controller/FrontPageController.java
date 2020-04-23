package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.util.IPUtils;
import com.guo.traveldemo.web.dto.RouteDTO;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.*;
import com.guo.traveldemo.web.service.Impl.CommonServiceImpl;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.RecommentService;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 前台控制页面
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/3/20
 */
@Controller
public class FrontPageController {

    @Autowired
    private RecommentService recommentService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/index")
    public String index(Model model, HttpSession session, HttpServletRequest request)throws Exception{
        if(session.getAttribute("city") != null){
            model.addAttribute("city",session.getAttribute("city"));
        }else{
            String ip = IPUtils.getIpAddr(request);
            //ip=218.192.3.42&json=true
            String content = "ip="+ip+"&json=true";
            String city = IPUtils.getAddresses(content,"GBK");
            if(city != null){
                model.addAttribute("city",city);
                // 保存在session中，避免过度使用API在
                session.setAttribute("city",city);
            }else{
                model.addAttribute("city","北京");
            }
        }
        // 返回游记推荐
        List<StrategyRecomd> list = recommentService.getList(2,1);
        List<StrategyDTO> result = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            StrategyDTO strategyDTO = new StrategyDTO();
            BeanUtils.copyProperties(list.get(i),strategyDTO);
            User user = userMapper.selectByPrimaryKey(list.get(i).getUserId());
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
        // TODO 轮播图未完成
        model.addAttribute("recommend",result);
        return "front/index";
    }
    @GetMapping("/strategydetail")
    public String detail(Model model,HttpSession session,int id,int detailId){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            boolean colstate = commonService.queryCollect(user.getId(),(byte)1,id);
            model.addAttribute("colstate",colstate);
        }
        // 热度增加
        redisService.addHot(id, "1",Constants.ESSAY_HOT_NAME);
        // 页面数据封装
        Strategy strategy = strategyService.selectStrategyById(id);
        // 攻略明细ID，用于获取评论
        model.addAttribute("id",strategy.getId());
        model.addAttribute("detailId",detailId);
        model.addAttribute("title",strategy.getTitle());
        User author = userMapper.getUserInfoByPrimaryKey(strategy.getUserId());
        model.addAttribute("authorId",author.getId());
        model.addAttribute("authorName",author.getName());
        model.addAttribute("authorImg",author.getImgUrl());
        // 获取文章热度/评论数/收藏数目
        Number hotNum = redisService.getScore(Constants.ESSAY_HOT_NAME, id);
        if(!Objects.isNull(hotNum)){
            model.addAttribute("hotnum",hotNum.intValue());
        }else{
            model.addAttribute("hotnum",0);
        }
        Number colNum = redisService.getViewNum(id, CollectionKey.ESSAY_KEY_COL_NUM);
        if(!Objects.isNull(colNum)){
            model.addAttribute("colnum",colNum.intValue());
        }else{
            model.addAttribute("colnum",0);
        }
        Number comNum = redisService.getViewNum(id, CollectionKey.ESSAY_KEY_COM_NUM);
        if(!Objects.isNull(comNum)){
            model.addAttribute("comnum",comNum.intValue());
        }else{
            model.addAttribute("comnum",0);
        }
        StrategyDetail detail = strategyService.getDetailById(detailId);
        if(strategyService != null){
            // 得到table
            TravelTable travelTable = strategyService.getTavelTableById(detail.getTableId());
            // 得到路线表
            Route route = strategyService.getRouteById(detail.getRouteId());
            RouteDTO routeDTO = new RouteDTO(route.getContent());
            model.addAttribute("route",routeDTO.getList());
            model.addAttribute("table",travelTable);
            model.addAttribute("content",detail.getContent());
        }
        return "front/strategy-detail";
    }
    @RequestMapping("/group")
    public String group(){
        return "front/group";
    }
    @RequestMapping("/group_detail")
    public String group_detail(){
        return "front/group-detail";
    }
    @RequestMapping("/topic_detail")
    public String topic(){
        return "front/topic-detail";
    }
    @RequestMapping("/question")
    public String question(){
        return "front/question";
    }
    @RequestMapping("/question_detail")
    public String question_detail(){
        return "front/question_detail";
    }
    @RequestMapping("/newquestion")
    public String newquestion(){
        return "front/newQuestion";
    }
    @RequestMapping("/newgroup")
    public String newgroup(){
        return "front/newGroup";
    }
    @RequestMapping("/newtopic")
    public String newtopic(){


        return "front/newTopic";
    }
    @RequestMapping("/newstrategy")
    public String newstrategy(){
        return "front/newStrategy";
    }
    @RequestMapping("/userInfo")
    public String user(Model model,int id){
        User user = null;
        if(!Objects.isNull(id)){
            user = userMapper.selectByPrimaryKey(id);
        }
        // TODO 可能不需要暴露整个用户信息
        model.addAttribute("info",user);
        return "user/index";
    }
    @RequestMapping("/usergroup")
    public String usergroup(){
        return "user/group";
    }
    @RequestMapping("/userquestion")
    public String userquestion(){
        return "user/question";
    }
    @RequestMapping("/userstrategy")
    public String userstrategy(){
        return "user/strategy";
    }
    @RequestMapping("/userset")
    public String userset(HttpSession session,Model model){
        return "user/set";
    }
    @RequestMapping("/usermsg")
    public String usermsg(){
        return "user/message";
    }
    @RequestMapping("/reg")
    public String reg(){
        return "reg";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

}
