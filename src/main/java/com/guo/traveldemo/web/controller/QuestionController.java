package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.pojo.Question;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

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

    /**
     * 发表问题
     * @param question
     * @param session
     * @return
     */
    @PostMapping("/newquestion")
    @ResponseBody
    public Response<String> newQuestion(Question question, HttpSession session) {
        Question result = new Question();
        BeanUtils.copyProperties(question,result);
        result.setDate(new Date());
        result.setFlag(Constants.QUESTION_NO);
        User user = (User)session.getAttribute("userinfo");
        result.setUserId(user.getId());
        return questionService.newQuestion(result);
    }
}
