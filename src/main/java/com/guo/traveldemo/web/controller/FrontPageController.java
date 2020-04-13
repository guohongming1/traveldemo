package com.guo.traveldemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 前台控制页面
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/3/20
 */
@Controller
public class FrontPageController {
    @RequestMapping("/index")
    public String index(){
        return "front/index";
    }
    @RequestMapping("/detail")
    public String detail(){
        return "front/strategy-detail";
    }
    @RequestMapping("/group")
    public String group(){
        return "front/group";
    }
    @RequestMapping("/group_detail")
    public String group_detail(){
        return "front/group-detail";
    }
    @RequestMapping("/topic_detail")
    public String topic(){
        return "front/topic-detail";
    }
    @RequestMapping("/question")
    public String question(){
        return "front/question";
    }
    @RequestMapping("/question_detail")
    public String question_detail(){
        return "front/question_detail";
    }
    @RequestMapping("/newquestion")
    public String newquestion(){
        return "front/newQuestion";
    }
    @RequestMapping("/newgroup")
    public String newgroup(){
        return "front/newGroup";
    }
    @RequestMapping("/newtopic")
    public String newtopic(){
        return "front/newTopic";
    }
    @RequestMapping("/newstrategy")
    public String newstrategy(){
        return "front/newStrategy";
    }
    @RequestMapping("/user")
    public String user(){
        return "user/index";
    }
    @RequestMapping("/usergroup")
    public String usergroup(){
        return "user/group";
    }
    @RequestMapping("/userquestion")
    public String userquestion(){
        return "user/question";
    }
    @RequestMapping("/userstrategy")
    public String userstrategy(){
        return "user/strategy";
    }
    @RequestMapping("/userset")
    public String userset(){
        return "user/set";
    }
    @RequestMapping("/usermsg")
    public String usermsg(){
        return "user/message";
    }
    @RequestMapping("/reg")
    public String reg(){
        return "reg";
    }
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
