package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.util.ScenicApiUtil;
import com.guo.traveldemo.web.dto.CommentDTO;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.dto.TableResultDTO;
import com.guo.traveldemo.web.mapper.CollectMapper;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.*;
import com.guo.traveldemo.web.service.Impl.CommonServiceImpl;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.QuestionService;
import com.guo.traveldemo.web.service.RecommentService;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/18
 */

@Controller
@RequestMapping("/comm")
public class CommonController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RecommentService recommentService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StrategyService strategyService;
    @Autowired
    private CommonServiceImpl commonServicel;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private QuestionService questionService;

    /**
     * 根据景点名称定位地址
     */
    @PostMapping("/local")
    @ResponseBody
    public Response<String> getLocal(String scenic){
        try {
            String name = ScenicApiUtil.getScenicInfoByURL(scenic,"全国");
            if(null != name && !"".equals(name) ){
                return Response.success(name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.fail(CodeMsg.FAIL);
    }

    @PostMapping("/strategy-list")
    @ResponseBody
    @Transactional
    public TableResultDTO<List<StrategyDTO>> strategyList(int limit,int page){
         if(limit > 0 && page > 0){
             // 返回游记推荐
             List<StrategyRecomd> list = recommentService.getList(limit,page);
             List<StrategyDTO> result = new ArrayList<>();
             for(int i=0;i<list.size();i++){
                 StrategyDTO strategyDTO = new StrategyDTO();
                 BeanUtils.copyProperties(list.get(i),strategyDTO);
                 User user = userMapper.selectByPrimaryKey(list.get(i).getUserId());
                 strategyDTO.setUserHeadImg(user.getImgUrl());
                 strategyDTO.setUserName(user.getName());
                 Number hotNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_HOT);
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
                 if(!Objects.isNull(colNum)){
                     strategyDTO.setCommentnum(comNum.intValue());
                 }else{
                     strategyDTO.setCommentnum(0);
                 }
                 result.add(strategyDTO);
             }
             return new TableResultDTO<>(200, "", page+1, result);
         }
        return new TableResultDTO<>(500, "参数错误", 0, null);
    }

    /**
     * 获取热度前十的攻略
     * @return
     */
    @PostMapping("/hotstra-list")
    @ResponseBody
    public Response<List<Strategy>> topRecommendTenList() {
        List<Integer> listId = redisService.getTopNum(Constants.ESSAY_HOT_NAME);
        List<Strategy> result = new ArrayList<>();
        if(listId != null && listId.size()>0){
            listId.forEach(id->result.add(strategyService.selectStrategyById(id)));
        }
        return Response.success(result);
    }
    @PostMapping("/around_strategy")
    @ResponseBody
    public Response<List<Strategy>> aroundStrategy(HttpSession session) {
        String address = null;
        if(session.getAttribute("city") != null){
            address = (String)session.getAttribute("city") ;
        }else{
            address = "北京";
        }
        return Response.success(strategyService.getList(10,1,address));
    }
    @PostMapping("/besnew_strategy")
    @ResponseBody
    public Response<List<Strategy>> bestNewStrategy() {
        return Response.success(strategyService.getList(10,1,null));
    }

    @PostMapping("/strcomment")
    @ResponseBody
    public Response<List<CommentDTO>> straComment(int detailId, int page){
        return commonServicel.straComment(detailId,page);
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
            redisService.setCommentNum(id,CollectionKey.ESSAY_KEY_COM_NUM);
            Strategy strategy = strategyService.selectStrategyById(id);
            //组装消息
            String msgcontent = "<a href='/userinfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>评论了您的攻略:"+
                    "<a href='/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>";
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
        String msgcontent = "来自攻略"+"<a href='/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>"+"回复您："+content;
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
            String msgcontent = "<a href='/userinfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>收藏了您的攻略:"+
                    "<a href='/strategydetail?id="+strategy.getId()+"&detailId=+"+strategy.getDetailId()+"'><cite>"+strategy.getTitle()+"</cite></a>";
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
    // userslive
    @PostMapping("/userslive")
    @ResponseBody
    public Response<Map<String,Object>> userslive(int id){
        Map<String,Object> result = new HashMap<>();
        if(!Objects.isNull(id)){
            List<Strategy> strategyList = strategyService.getStrategyByUserId(id);
            result.put("strategy",strategyList);
            List<Question> questionList = questionService.getQuestionListByUserId(id);
            result.put("question",questionList);
            return Response.success(result);
        }
       return Response.fail(CodeMsg.FAIL);
    }
}
