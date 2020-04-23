package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.mapper.QuestionMapper;
import com.guo.traveldemo.web.pojo.Question;
import com.guo.traveldemo.web.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return questionMapper.selectList(query);
    }
}
