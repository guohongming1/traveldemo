package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.result.StrategyDTO;
import com.guo.traveldemo.web.mapper.CollectMapper;
import com.guo.traveldemo.web.pojo.*;
import com.guo.traveldemo.web.service.Impl.CommonServiceImpl;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommonServiceImpl commonServicel;
    @Autowired
    private CollectMapper collectMapper;

    @RequestMapping("/newstrategy")
    public String newstrategy(){
        return "front/newStrategy";
    }
    /**
     * 创建攻略
     * @param strategyDTO
     * @return
     */
    @PostMapping("/post")
    @ResponseBody
    public Response<String> postStrategy(StrategyDTO strategyDTO,HttpSession session){
        // 未发表不需要进行数据校验
        if(null != strategyDTO.getPushFlag()&& strategyDTO.getPushFlag()){
            // 后台数据校验
            if(null == strategyDTO || strategyDTO.getTitle() == null || strategyDTO.getAddress() == null
                    || strategyDTO.getContent() == null || strategyDTO.getHeadImgUrl() ==null){
                Response.fail(CodeMsg.FAIL);
            }
        }
        Strategy strategy = new Strategy();
        if(strategyDTO.getPushFlag()){
            strategy.setPushFlag(Constants.PUSH_YES);
        }else{
            strategy.setPushFlag(Constants.PUSH_NO);
        }
        strategy.setTitle(strategyDTO.getTitle());
        strategy.setHeadImgUrl(strategyDTO.getHeadImgUrl());
        strategy.setAddress(strategyDTO.getAddress());
        strategy.setSketch(strategyDTO.getSketch());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sf.parse(strategyDTO.getDate());
            strategy.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = (User)session.getAttribute("userinfo");
        strategy.setUserId(user.getId());
        //
        TravelTable travelTable = new TravelTable();
        travelTable.setAddress(strategyDTO.getAddress());
        travelTable.setDays(strategyDTO.getDays());
        travelTable.setFee(strategyDTO.getMoney());
        travelTable.setPeople(strategyDTO.getPeople());
        travelTable.setAdvice(strategyDTO.getAdvice());
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sf.parse(strategyDTO.getDate());
            travelTable.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strategyService.createStrategy(strategy,travelTable,strategyDTO.getRoute(),strategyDTO.getContent());
    }
    /**
     * 更新攻略
     * @param strategyDTO
     * @return
     */
    @PostMapping("/updateorsave")
    @ResponseBody
    public Response<String> updateStrategy(StrategyDTO strategyDTO,HttpSession session){
        Strategy strategy = new Strategy();
        if(strategyDTO.getPushFlag()){
            strategy.setPushFlag(Constants.PUSH_YES);
        }else{
            strategy.setPushFlag(Constants.PUSH_NO);
        }
        strategy.setTitle(strategyDTO.getTitle());
        strategy.setHeadImgUrl(strategyDTO.getHeadImgUrl());
        strategy.setAddress(strategyDTO.getAddress());
        strategy.setSketch(strategyDTO.getSketch());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            if(null != strategyDTO.getDate()){
                Date date = sf.parse(strategyDTO.getDate());
                strategy.setDate(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = (User)session.getAttribute("userinfo");
        strategy.setUserId(user.getId());
        //
        TravelTable travelTable = new TravelTable();
        travelTable.setAddress(strategyDTO.getAddress());
        travelTable.setDays(strategyDTO.getDays());
        travelTable.setFee(strategyDTO.getMoney());
        travelTable.setPeople(strategyDTO.getPeople());
        travelTable.setAdvice(strategyDTO.getAdvice());
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            if(null != strategyDTO.getDate()){
                Date date = sf.parse(strategyDTO.getDate());
                strategy.setDate(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strategyService.updateStrategy(strategy,travelTable,strategyDTO.getRoute(),strategyDTO.getContent());
    }
    /**
     * 拉取上次未发表攻略
     * @param session
     * @return
     */
    @PostMapping("/pull")
    @ResponseBody
    public Response<StrategyDTO> pullStrategy(HttpSession session){
        User user = (User)session.getAttribute("userinfo");
        return strategyService.pullStrategy(user.getId());
    }
    /**
     * 攻略评论
     * @param session
     * @param id 攻略id
     * @return
     */
    @PostMapping("/substracomment")
    @ResponseBody
    public Response<String> substracomment(HttpSession session,int id,String content){
        User user = (User)session.getAttribute("userinfo");
        if(!Objects.isNull(id)){
            redisService.addHot(id, "2",Constants.ESSAY_HOT_NAME);//增加热度
            // 增加评论数
            redisService.setCommentNum(id, CollectionKey.ESSAY_KEY_COM_NUM);
            Strategy strategy = strategyService.selectStrategyById(id);
            //组装消息
            String msgcontent = "<a href='/userInfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>评论了您的攻略:"+
                    "<a href='/front/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>";
            //发送消息
            if(strategy.getUserId() != user.getId()){
                //作者本人发送评论不需要发送通知
                messageService.sendRemind(id,Constants.STRATEGY_MSG,Constants.COM_MSG,user.getId(),msgcontent);
            }
            StrategyComment strategyComment = new StrategyComment();
            strategyComment.setStraDeId(strategy.getDetailId());
            strategyComment.setContent(content);
            strategyComment.setUserId(user.getId());
            strategyComment.setDate(new Date());
            // 插入评论
            commonServicel.insertStrategyComment(strategyComment);
            return Response.success("发表成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }
    @PostMapping("/repstracomment")
    @ResponseBody
    @Transactional
    public Response<String> repstracomment(HttpSession session,int straId,int id,String content){
        User user = (User)session.getAttribute("userinfo");
        StrategyComment strategyComment = commonServicel.queryById(id);
        strategyComment.setReply(content);
        //组装消息
        Strategy strategy = strategyService.selectStrategyById(straId);
        String msgcontent = "来自攻略"+"<a href='/front/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>"+"回复您："+content;
        //发送消息
        messageService.sendMsg(user.getId(),strategyComment.getUserId(),msgcontent);
        commonServicel.updateStraComment(strategyComment);
        return Response.success("成功");
    }

    /**
     * 攻略收藏
     * @param session
     * @param straId 攻略表ID
     * @return
     */
    @PostMapping("/straCollect")
    @ResponseBody
    public Response<String> straCollect(HttpSession session,int straId){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            redisService.addHot(straId, "3",Constants.ESSAY_HOT_NAME);//增加热度
            Strategy strategy = strategyService.selectStrategyById(straId);
            //组装消息
            String msgcontent = "<a href='/userInfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>收藏了您的攻略:"+
                    "<a href='/front/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>";
            //发送消息
            messageService.sendRemind(straId,Constants.STRATEGY_MSG,Constants.COLLECT_MSG,user.getId(),msgcontent);
            Collect collect = new Collect();
            collect.setDate(new Date());
            collect.setProId(straId);
            collect.setType((byte)1);
            collect.setUserId(user.getId());
            collectMapper.insertSelective(collect);
            //收藏数量加一
            redisService.setCollectNum(straId,CollectionKey.ESSAY_KEY_COL_NUM);
            return Response.success("成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }
}
