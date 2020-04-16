package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guo.traveldemo.web.pojo.Question;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionMapper extends BaseMapper<Question> {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);
}