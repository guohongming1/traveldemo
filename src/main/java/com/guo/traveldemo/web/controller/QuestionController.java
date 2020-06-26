package com.guo.traveldemo.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.QuestionCommentDTO;
import com.guo.traveldemo.web.dto.QuestionDTO;
import com.guo.traveldemo.web.dto.TableResultDTO;
import com.guo.traveldemo.web.mapper.CollectMapper;
import com.guo.traveldemo.web.mapper.MsgConfigMapper;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.*;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.MessageService;
import com.guo.traveldemo.web.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MsgConfigMapper msgConfigMapper;


    @RequestMapping("/newquestion")
    public String newquestion(){
        return "front/newQuestion";
    }

    /**
     * 发表问题
     * @param question
     * @param session
     * @return
     */
    @PostMapping("/submitquestion")
    @ResponseBody
    public Response<String> submitquestion(Question question, HttpSession session) {
        Question result = new Question();
        BeanUtils.copyProperties(question,result);
        result.setDate(new Date());
        result.setFlag(Constants.QUESTION_NO);
        User user = (User)session.getAttribute("userinfo");
        result.setUserId(user.getId());
        return questionService.newQuestion(result);
    }


    /**
     * 删除评论
     * @param session
     * @param id
     * @return
     */
    @PostMapping("/delQuestionComment")
    @ResponseBody
    public Response<String> delQuestionCommment(HttpSession session,int id,int questionId){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            if(questionService.delQuestionCommment(id,user.getId())==1){
                redisService.reCollectOrCommentNum(questionId,CollectionKey.QUESTION_KEY_COM_NUM);
                return Response.success("删除成功");
            }
            if(questionService.delQuestionCommment(id,user.getId())==-1){
                return Response.success("最佳答案不允许删除！");
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 创建评论
     * @param session
     * @param questionId
     * @param content
     * @return
     */
    @PostMapping("/createQuestionCommment")
    @ResponseBody
    public Response<String> createQuestionCommment(HttpSession session,int questionId,String content,String userIds){
        User user = (User)session.getAttribute("userinfo");
        String[] userId = null;
        Question question = questionService.selectById(questionId);
        if(question !=null && user != null && !("").equals(userIds)){
            userId = userIds.split("-");
            // 发送通知
            for(int i=0;i<userId.length;i++){
                String msg ="<a href='/userinfo?id="+user.getId()+"'><cite>"+user.getName()+ "</cite></a>在问答："+"<a href='/front/questionDetail?id="+question.getId()+"'><cite>"+question.getTitle()+"</cite></a>"+
                        "回复您："+content;
                messageService.sendMsg(user.getId(),Integer.valueOf(userId[i]),msg);
            }
        }
        if(user != null && questionService.createQuestionCommment(user.getId(),questionId,content)>0){
            //评论数量加一
            redisService.setCommentNum(questionId,CollectionKey.QUESTION_KEY_COM_NUM);
            //问答排名加一
            redisService.addQuestionCommentNum(user.getId(),Constants.USER_QUESTION_COM_NUM);
            // 热度增加
            redisService.addHot(questionId,"2",Constants.QUESTION_HOT_NAME);
            // 发送消息通知全部关注此问题的用户
            String msgcontent = "来自问答："+"<a href='/front/questionDetail?id="+question.getId()+"'><cite>"+question.getTitle()+"</cite></a>"+"评论有更新，请注意关注";
            messageService.sendRemind(questionId,Constants.QUESTION_MSG,Constants.COM_MSG,user.getId(),msgcontent);
            return Response.success("成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 采纳评论
     * @param session
     * @param Id 评论表的id
     * @param questionId 问题表的id
     * @return
     */
    @PostMapping("/acptComment")
    @ResponseBody
    public Response<String> acptComment(HttpSession session,int Id,int questionId){
        User user = (User)session.getAttribute("userinfo");
        Question question = questionService.selectById(questionId);
        if(question!=null && user != null && questionService.acptComment(Id,questionId,user.getId())>0){
            String msgcontent = "问题："+"<a href='/front/questionDetail?id="+question.getId()+"'><cite>"+question.getTitle()+"</cite></a>"+"已解决,请注意关注";
            messageService.sendRemind(questionId,Constants.QUESTION_MSG,Constants.QUESTION_FIN_MSG,user.getId(),msgcontent);
            return Response.success("成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 收藏问题
     * @param session
     * @param questionId
     * @return
     */
    @PostMapping("/collectQuestion")
    @ResponseBody
    public Response<String> collectQuestion(HttpSession session,int questionId){
        User user = (User)session.getAttribute("userinfo");
        QueryWrapper<Collect> query = new QueryWrapper<>();
        query.eq("user_id",user.getId());
        query.eq("type",(byte)2);
        query.eq("pro_id",questionId);
        if(collectMapper.selectCount(query)>0){
            return Response.fail(CodeMsg.FAIL);
        }
        if(user != null){
            Question question = questionService.selectById(questionId);
            if(question != null){
                //组装消息
                String msgcontent = "<a href='/userInfo?id="+user.getId()+"'><cite>"+user.getName()+"</cite></a>收藏了您的提问:"+
                        "<a href='/front/questionDetail?id="+question.getId()+"'><cite>"+question.getTitle()+"</cite></a>";
                //发送消息
                messageService.sendMsg(user.getId(),question.getUserId(),msgcontent);
                // 插入收藏表
                Collect collect = new Collect();
                collect.setFlag(Constants.ACPT);
                collect.setUserId(user.getId());
                collect.setType((byte)2);
                collect.setProId(questionId);
                collect.setDate(new Date());
                collectMapper.insertSelective(collect);
                //收藏数量加一
                redisService.setCollectNum(questionId,CollectionKey.QUESTION_KEY_COM_NUM);
                return Response.success("成功");
            }
            redisService.addHot(questionId, "3",Constants.QUESTION_HOT_NAME);//增加热度
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 获取个人问答/收藏
     * @param session
     * @return
     */
    @PostMapping("/getPerquestion")
    @ResponseBody
    public Response<Map<String,Object>> getPerstrategy(HttpSession session){
        User sessionuser = (User)session.getAttribute("userinfo");
        Map<String,Object> map = new HashMap<>();
        List<Question> list = questionService.getQuestionListByUserId(sessionuser.getId());
        map.put("question",list);
        QueryWrapper<Collect> query = new QueryWrapper<>();
        query.eq("user_id",sessionuser.getId());
        query.eq("type",(byte)2);
        List<Collect> collectList = collectMapper.selectList(query);
        List<Integer> colList = new ArrayList<>();
        if(collectList != null && collectList.size()>0){
            collectList.forEach(item->colList.add(item.getProId()));
        }
        List<Question> colQuestionList = new ArrayList<>();
        colList.forEach(item->{
            colQuestionList.add(questionService.selectById(item));
        });
        map.put("colquestion",colQuestionList);
        return Response.success(map);
    }
    @PostMapping("/delQuestion")
    @ResponseBody
    public Response<String> delQuestion(HttpSession session,int id,int type){
        User user = (User)session.getAttribute("userinfo");
        if(user != null){
            if(type == 1){//删除自己的问答消息 非物理删除
                Question question = questionService.selectById(id);
                if(question.getUserId() == user.getId()) {//验证问答
                    question.setFlag((byte)3);
                    if(questionService.updateQuestion(question)>0){
                        return Response.success("删除成功");
                    }
                }
            }
            if(type == 2){
                // 物理删除
                QueryWrapper<Collect> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id",user.getId());
                queryWrapper.eq("type",(byte)2);
                queryWrapper.eq("pro_id",id);
                Collect collect = collectMapper.selectOne(queryWrapper);
                if(collect != null && collect.getUserId()==user.getId()){
                    if(collectMapper.deleteByPrimaryKey(collect.getId())>0){
                        return Response.success("删除成功");
                    }
                }
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }
    /**
     * 消息设置
     * @param id
     * @return
     */
    public Response<String> questionMsgConfig(HttpSession session,int id,int type){
            User user = (User)session.getAttribute("userinfo");
            if(user != null){
            if(type == 1){//屏蔽自己的问答消息
                Question question = questionService.selectById(id);
                if(question.getUserId() == user.getId()){//验证问答
                    QueryWrapper<MsgConfig> query = new QueryWrapper<>();
                    query.eq("user_id",user.getId());
                    query.eq("target_id",id);
                    query.eq("type","1");
                    MsgConfig msgConfig = msgConfigMapper.selectOne(query);
                    if(msgConfig == null){
                        // TODO
                    }
                }
            }
        }
        return null;
    }
    /**
     * 组装前端数据
     * @param list
     * @return
     */
    public List<QuestionDTO> setQuestionDTOList(List<Question> list){
        List<QuestionDTO> result = new ArrayList<>();
        list.forEach(item->{
            User user = userMapper.getUserInfoByPrimaryKey(item.getUserId());
            QuestionDTO dto = new QuestionDTO();
            BeanUtils.copyProperties(item,dto);
            if(dto.getImgUrl() == null || dto.getImgUrl() == ""){
                // 如果没有问题图像就使用用户头像
                dto.setImgUrl(user.getImgUrl());
            }
            dto.setUserName(user.getName());
            // 获取热度
            Number hotNum = redisService.getScore(Constants.QUESTION_HOT_NAME, item.getId());
            if(!Objects.isNull(hotNum)){
                dto.setHotNum(hotNum.intValue());
            }else{
                dto.setHotNum(0);
            }
            // 获取评论数目
            Number comNum = redisService.getViewNum(item.getId(), CollectionKey.QUESTION_KEY_COM_NUM);
            if(!Objects.isNull(comNum)){
                dto.setCommentNum(comNum.intValue());
            }else{
                dto.setCommentNum(0);
            }
            result.add(dto);
        });
        return result;
    }
    public List<QuestionCommentDTO> setCommentData(List<QuestionComment> list){
        List<QuestionCommentDTO> result = new ArrayList<>();
        list.forEach(item->{
            User user = userMapper.getUserInfoByPrimaryKey(item.getUserId());
            QuestionCommentDTO dto = new QuestionCommentDTO();
            BeanUtils.copyProperties(item,dto);
            dto.setUserName(user.getName());
            dto.setUserImg(user.getImgUrl());
            result.add(dto);
        });
        return result;
    }
}
