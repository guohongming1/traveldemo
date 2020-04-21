package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.result.StrategyDTO;
import com.guo.traveldemo.web.pojo.Route;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.TravelTable;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Controller
@RequestMapping("/strategy")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    /**
     * 创建攻略
     * @param strategyDTO
     * @return
     */
    @PostMapping("/post")
    @ResponseBody
    public Response<String> postStrategy(StrategyDTO strategyDTO,HttpSession session){
        // 未发表不需要进行数据校验
        if(null != strategyDTO.getPushFlag()&& strategyDTO.getPushFlag()){
            // 后台数据校验
            if(null == strategyDTO || strategyDTO.getTitle() == null || strategyDTO.getAddress() == null
                    || strategyDTO.getContent() == null || strategyDTO.getHeadImgUrl() ==null){
                Response.fail(CodeMsg.FAIL);
            }
        }
        Strategy strategy = new Strategy();
        if(strategyDTO.getPushFlag()){
            strategy.setPushFlag(Constants.PUSH_YES);
        }else{
            strategy.setPushFlag(Constants.PUSH_NO);
        }
        strategy.setTitle(strategyDTO.getTitle());
        strategy.setHeadImgUrl(strategyDTO.getHeadImgUrl());
        strategy.setAddress(strategyDTO.getAddress());
        strategy.setSketch(strategyDTO.getSketch());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sf.parse(strategyDTO.getDate());
            strategy.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = (User)session.getAttribute("userinfo");
        strategy.setUserId(user.getId());
        //
        TravelTable travelTable = new TravelTable();
        travelTable.setAddress(strategyDTO.getAddress());
        travelTable.setDays(strategyDTO.getDays());
        travelTable.setFee(strategyDTO.getMoney());
        travelTable.setPeople(strategyDTO.getPeople());
        travelTable.setAdvice(strategyDTO.getAdvice());
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            Date date = sf.parse(strategyDTO.getDate());
            travelTable.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strategyService.createStrategy(strategy,travelTable,strategyDTO.getRoute(),strategyDTO.getContent());
    }
    /**
     * 更新攻略
     * @param strategyDTO
     * @return
     */
    @PostMapping("/updateorsave")
    @ResponseBody
    public Response<String> updateStrategy(StrategyDTO strategyDTO,HttpSession session){
        Strategy strategy = new Strategy();
        if(strategyDTO.getPushFlag()){
            strategy.setPushFlag(Constants.PUSH_YES);
        }else{
            strategy.setPushFlag(Constants.PUSH_NO);
        }
        strategy.setTitle(strategyDTO.getTitle());
        strategy.setHeadImgUrl(strategyDTO.getHeadImgUrl());
        strategy.setAddress(strategyDTO.getAddress());
        strategy.setSketch(strategyDTO.getSketch());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            if(null != strategyDTO.getDate()){
                Date date = sf.parse(strategyDTO.getDate());
                strategy.setDate(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        User user = (User)session.getAttribute("userinfo");
        strategy.setUserId(user.getId());
        //
        TravelTable travelTable = new TravelTable();
        travelTable.setAddress(strategyDTO.getAddress());
        travelTable.setDays(strategyDTO.getDays());
        travelTable.setFee(strategyDTO.getMoney());
        travelTable.setPeople(strategyDTO.getPeople());
        travelTable.setAdvice(strategyDTO.getAdvice());
        try {
            //使用SimpleDateFormat的parse()方法生成Date
            if(null != strategyDTO.getDate()){
                Date date = sf.parse(strategyDTO.getDate());
                strategy.setDate(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strategyService.updateStrategy(strategy,travelTable,strategyDTO.getRoute(),strategyDTO.getContent());
    }
    /**
     * 拉取上次未发表攻略
     * @param session
     * @return
     */
    @PostMapping("/pull")
    @ResponseBody
    public Response<StrategyDTO> pullStrategy(HttpSession session){
        User user = (User)session.getAttribute("userinfo");
        return strategyService.pullStrategy(user.getId());
    }
}
