package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.mapper.QuestionCommentMapper;
import com.guo.traveldemo.web.mapper.QuestionMapper;
import com.guo.traveldemo.web.pojo.Question;
import com.guo.traveldemo.web.pojo.QuestionComment;
import com.guo.traveldemo.web.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionCommentMapper commentMapper;
    @Autowired
    private RedisService redisService;

    /**
     * 发表问题
     * @param question
     * @return
     */
    @Override
    public Response<String> newQuestion(Question question) {
        if(questionMapper.insertSelective(question)>0){
            return Response.success("成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }

    @Override
    public List<Question> getQuestionListByUserId(int id) {
        QueryWrapper<Question> query = new QueryWrapper<>();
        query.eq("user_id",id);
        query.ne("flag",(byte)3);
        return questionMapper.selectList(query);
    }
    @Override
    public List<Question> queryList(QueryWrapper<Question> query){
        return questionMapper.selectList(query);
    }

    @Override
    public Question selectById(int id) {
        return questionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Question> selectPageVo(int limit, int page, String address,Byte flag) {
        // 分页
        Page<Question> pageHelper = new Page<>();
        pageHelper.setSize(limit);
        pageHelper.setCurrent(page);
        IPage<Question> pageVo = questionMapper.selectPageVo(pageHelper,address,flag);
        return pageVo.getRecords();
    }

    @Override
    public int getCount(QueryWrapper<Question> query) {
        return questionMapper.selectCount(query);
    }

    /**
     * 加载评论
     * @param questionId
     * @return
     */
    @Override
    public List<QuestionComment> getQuestionComment(int questionId){
        // 先找出被采纳的评论
        QueryWrapper<QuestionComment> answerQuery = new QueryWrapper<>();
        answerQuery.eq("question_id",questionId);
        answerQuery.eq("flag", Constants.NOM_ANSWER);
        answerQuery.orderByDesc("date");
        List<QuestionComment> commentList = commentMapper.selectList(answerQuery);
        QueryWrapper<QuestionComment> besanswerQuery = new QueryWrapper<>();
        besanswerQuery.eq("question_id",questionId);
        besanswerQuery.eq("flag", Constants.BEST_ANSWER);
        besanswerQuery.orderByDesc("date");
        List<QuestionComment> bestAnswerList = commentMapper.selectList(besanswerQuery);
        if(commentList != null && commentList.size()>0){
            commentList.forEach(item->bestAnswerList.add(item));
        }
        return bestAnswerList;
    }

    /**
     * 删除评论
     * @param id 评论表ID
     * @param userId
     * @return
     */
    public int delQuestionCommment(int id,int userId){
        QuestionComment questionComment = commentMapper.selectByPrimaryKey(id);
        // 验证该条评论是否是本人删除
        if(questionComment != null && questionComment.getUserId() == userId){
            if(questionComment.getFlag() != Constants.BEST_ANSWER){
                return commentMapper.deleteByPrimaryKey(id);
            }else{
                return -1; //最佳答案不允许删除
            }
        }
        return 0;
    }

    /**
     * 创建评论
     * @param userId
     * @param questionId
     * @param content
     * @return
     */
    public int createQuestionCommment(int userId,int questionId,String content){
        QuestionComment questionComment = new QuestionComment();
        questionComment.setUserId(userId);
        questionComment.setQuestionId(questionId);
        questionComment.setContent(content);
        questionComment.setDate(new Date());
        questionComment.setFlag(Constants.NOM_ANSWER);
        return commentMapper.insertSelective(questionComment);
    }

    /**
     * 采纳评论
     * @param Id 评论表ID
     * @param questionId 问题表ID
     * @param userId 操作者ID
     * @return
     */
    @Transactional
    public int acptComment(int Id,int questionId,int userId){
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if(question != null && question.getUserId() == userId){
            question.setFlag(Constants.QUESTION_YES);
            questionMapper.updateByPrimaryKeySelective(question);
            QuestionComment questionComment = new QuestionComment();
            questionComment.setId(Id);
            questionComment.setFlag(Constants.BEST_ANSWER);
            return commentMapper.updateByPrimaryKeySelective(questionComment);
        }
        return 0;
    }
    public int updateQuestion(Question question){
        return questionMapper.updateByPrimaryKeySelective(question);
    }

    @Transactional
    public int delQuestionBatch(List<Integer> ids){
        // 删除评论
        ids.forEach(item->{
            QueryWrapper<QuestionComment> query = new QueryWrapper<>();
            query.eq("question_id",item);
            if(commentMapper.selectCount(query) >0){
                commentMapper.deleteByQuestionId(item);
            }
        });
        if(ids !=null && ids.size()>0){
            return questionMapper.deleteBatchIds(ids);
        }
        return 0;
    }


}
