package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guo.traveldemo.web.pojo.QuestionComment;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCommentMapper extends BaseMapper<QuestionComment> {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionComment record);

    int insertSelective(QuestionComment record);

    QuestionComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionComment record);

    int updateByPrimaryKey(QuestionComment record);

    @Select("delete from question_comment where question_id=#{questionId}")
    int deleteByQuestionId(int questionId);
}