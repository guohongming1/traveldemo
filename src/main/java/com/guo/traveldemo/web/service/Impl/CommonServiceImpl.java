package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.CommentDTO;
import com.guo.traveldemo.web.mapper.*;
import com.guo.traveldemo.web.pojo.Collect;
import com.guo.traveldemo.web.pojo.StrategyComment;
import com.guo.traveldemo.web.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/14
 */
@Service
public class CommonServiceImpl{

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private StrategyCommentMapper straComment;

    @Autowired
    private UserMapper userMapper;

    // 攻略id获取用户ID
    public int getUserIdByStrategyID(int id){
        return strategyMapper.selectByPrimaryKey(id).getUserId();
    }

    /**
     * 查询用户是否收藏
     * @param userId
     * @return
     */
    public Boolean queryCollect(int userId,byte type,int proId){
        QueryWrapper<Collect> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("type",type);
        query.eq("pro_id",proId);
        if(collectMapper.selectCount(query)>0){
            return true;
        }
        return false;
    }
    public List<Collect> getCollectList(int userId,byte type){
        QueryWrapper<Collect> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("type",type);
        return collectMapper.selectList(query);
    }
    /**
     * 获取攻略评论
     * @param detailId
     * @param page
     * @return
     */
    public Response<List<CommentDTO>> straComment(int detailId,int page){
        if(!Objects.isNull(detailId)){
            List<CommentDTO> result = new ArrayList<>();
            // 分页
            Page<StrategyComment> pageHelper = new Page<>();
            pageHelper.setSize(10);
            pageHelper.setCurrent(page);

            IPage<StrategyComment> pageVo = null;

            pageVo = straComment.selectPageVo(pageHelper,detailId);
            List<StrategyComment> list = pageVo.getRecords();
            list.forEach(item->{
                CommentDTO dto = new CommentDTO();
                BeanUtils.copyProperties(item,dto);
                User user = userMapper.getUserInfoByPrimaryKey(item.getUserId());
                dto.setUserId(user.getId());
                dto.setUserName(user.getName());
                dto.setUserImg(user.getImgUrl());
                result.add(dto);
            });
            return Response.success(result);
        }
        return Response.fail(CodeMsg.FAIL);
    }

    /**
     * 插入攻略评论
     * @param strategyComment
     * @return
     */
    public int insertStrategyComment(StrategyComment strategyComment){
        return straComment.insertSelective(strategyComment);
    }

    /**
     * 查询评论
     * @param id
     * @return
     */
    public StrategyComment queryById(int id){
        return straComment.selectByPrimaryKey(id);
    }
    public int updateStraComment(StrategyComment strategyComment){
        return straComment.updateByPrimaryKeySelective(strategyComment);
    }
}
