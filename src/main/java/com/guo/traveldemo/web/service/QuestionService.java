package com.guo.traveldemo.web.service;

import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.pojo.Question;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
public interface QuestionService {
    Response<String> newQuestion(Question question);
}
