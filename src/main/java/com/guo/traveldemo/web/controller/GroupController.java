package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.NewTopicDTO;
import com.guo.traveldemo.web.pojo.TravelGroup;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/newgroup")
    @ResponseBody
    public Response<String> createGroup(TravelGroup group, HttpSession session){
        group.setFlag(Constants.PASS_NO);
        group.setDate(new Date());
        User user = (User)session.getAttribute("userinfo");
        group.setUserId(user.getId());
        return groupService.createGroup(group);
    }

    @PostMapping("/newtopic")
    @ResponseBody
    public Response<String> createTopic(NewTopicDTO topicDTO) {
        if(!Objects.isNull(topicDTO)){
            return groupService.createTopic(topicDTO);
        }
        return Response.fail(100,"参数错误");
    }
}
