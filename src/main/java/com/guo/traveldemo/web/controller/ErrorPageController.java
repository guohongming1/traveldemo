package com.guo.traveldemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/25
 */
@Controller
public class ErrorPageController {
    @RequestMapping(value = "/error400Page")
    public String error400Page() {
        return "error/404";
    }
    @RequestMapping(value = "/error500Page")
    public String error500Page(Model model) {
        return "error/500";
    }
}
