package com.guo.traveldemo.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.NewTopicDTO;
import com.guo.traveldemo.web.pojo.*;
import net.sf.jsqlparser.statement.select.Top;

import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
public interface GroupService {
    Response<String> createGroup(TravelGroup group);

    Response<String> createTopic(NewTopicDTO topicDTO);

    Boolean checkGroupMById(int groupId, int userId);

    int updateTravelGroupById(TravelGroup group);

    List<Topic> selectPageVo(int limit,int page,String address);

    List<TravelGroup> selectPageVoGroup(int limit,int page);

    List<TravelGroup> selectPageVoGroupWithFlag(int limit,int page,Byte flag);

    int getGroupCount(QueryWrapper<TravelGroup> query);

    Topic queryTopicById(int id);

    TopicDetail queryTopicDetail(int id);

    List<GroupType> selectList(QueryWrapper<GroupType> query);

    List<TravelGroup> selectTravelGroup(QueryWrapper<TravelGroup> query);

    List<TravelGroup> selectJoinGroup(int userId);

    List<Topic> selectColGroup(int userId);

    List<Topic> selectUserTopic(int userId);

    List<Topic> queryListTopic(QueryWrapper<Topic> query);

    TravelGroup queryTravelGroupById(int id);

    List<Topic> queryTopicByGroupId(int id);

    int getTopicCount(QueryWrapper<Topic> query);

    int getCountMember(QueryWrapper<GroupMember> queryWrapper);
    int joinGroup(int userId,int groupId);
    List<TopicComment> selectTopicCommemnt(int topicDetailId);

    int createTopicComment(int topicId,int userId,String content);

    int delTopicComment(int id,int userId);

    int outGroup(int userId,int groupId);

    int delColTopic(int userId,int topicId);

    int disGroup(int userId,int groupId);

    int delTopic(int userId,int topicId);

    Topic queryTopic(int id);
}
