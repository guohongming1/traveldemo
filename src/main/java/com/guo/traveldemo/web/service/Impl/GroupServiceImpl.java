package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.NewTopicDTO;
import com.guo.traveldemo.web.mapper.GroupMemberMapper;
import com.guo.traveldemo.web.mapper.TopicDetailMapper;
import com.guo.traveldemo.web.mapper.TopicMapper;
import com.guo.traveldemo.web.mapper.TravelGroupMapper;
import com.guo.traveldemo.web.pojo.GroupMember;
import com.guo.traveldemo.web.pojo.Topic;
import com.guo.traveldemo.web.pojo.TopicDetail;
import com.guo.traveldemo.web.pojo.TravelGroup;
import com.guo.traveldemo.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private TravelGroupMapper groupMapper;

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private TopicDetailMapper topicDetailMapper;

    /**
     * 创建小组
     * @param group
     * @return
     */
    @Override
    public Response<String> createGroup(TravelGroup group) {
        if(groupMapper.insertSelective(group)>0){
            return Response.success("成功");
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 创建话题
     * @param topicDTO
     * @return
     */
    @Override
    @Transactional
    public Response<String> createTopic(NewTopicDTO topicDTO) {
        if(this.checkGroupMById(Integer.parseInt(topicDTO.getGroupId()),topicDTO.getUserId())){
            TravelGroup travelGroup = groupMapper.selectByPrimaryKey(Integer.parseInt(topicDTO.getGroupId()));
            if(travelGroup != null){
                travelGroup.setTopicNum(travelGroup.getTopicNum()==null? 0:travelGroup.getTopicNum()+1);
                //更新小组话题数量
                groupMapper.updateByPrimaryKeySelective(travelGroup); // TODO 最好更新指定字段
                // 话题明细
                TopicDetail detail = new TopicDetail();
                detail.setUserId(topicDTO.getUserId());
                detail.setContent(topicDTO.getContent());
                detail.setTitle(topicDTO.getTitle());
                detail.setDate(new Date());
                topicDetailMapper.insertSelective(detail);
                //话题表
                Topic topic = new Topic();
                topic.setTitle(topicDTO.getTitle());
                topic.setTags(topicDTO.getTags());
                topic.setGroupId(travelGroup.getId());
                topic.setGroupName(travelGroup.getTitle());
                topic.setUserId(topicDTO.getUserId());
                topic.setGrDeId(detail.getId());
                topicMapper.insertSelective(topic);
                return Response.success("成功");
            }
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 根据userid和groupId判断成员是否在小组内
     * @param groupId
     * @param userId
     * @return
     */
    public Boolean checkGroupMById(int groupId, int userId){
        QueryWrapper<GroupMember> query = new QueryWrapper<>();
        query.eq("group_id",groupId);
        query.eq("user_id",userId);
        if(groupMemberMapper.selectCount(query)>0){
            return true;
        }
        return false;
    }


}
