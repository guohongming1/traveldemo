package com.guo.traveldemo.web.service.Impl;

import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.mapper.TravelGroupMapper;
import com.guo.traveldemo.web.pojo.TravelGroup;
import com.guo.traveldemo.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private TravelGroupMapper groupMapper;

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
}
