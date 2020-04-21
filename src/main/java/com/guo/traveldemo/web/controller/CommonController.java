package com.guo.traveldemo.web.controller;

import com.guo.traveldemo.cache.CollectionKey;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.util.ScenicApiUtil;
import com.guo.traveldemo.web.dto.HotMap;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.dto.TableResultDTO;
import com.guo.traveldemo.web.mapper.StrategyMapper;
import com.guo.traveldemo.web.mapper.UserMapper;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyRecomd;
import com.guo.traveldemo.web.pojo.User;
import com.guo.traveldemo.web.service.Impl.RedisService;
import com.guo.traveldemo.web.service.RecommentService;
import com.guo.traveldemo.web.service.StrategyService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/18
 */

@Controller
@RequestMapping("/comm")
public class CommonController {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RecommentService recommentService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StrategyService strategyService;

    /**
     * 根据景点名称定位地址
     */
    @PostMapping("/local")
    @ResponseBody
    public Response<String> getLocal(String scenic){
        try {
            String name = ScenicApiUtil.getScenicInfoByURL(scenic,"全国");
            if(null != name && !"".equals(name) ){
                return Response.success(name);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return Response.fail(CodeMsg.FAIL);
    }

    @PostMapping("/strategy-list")
    @ResponseBody
    @Transactional
    public TableResultDTO<List<StrategyDTO>> strategyList(int limit,int page){
         if(limit > 0 && page > 0){
             // 返回游记推荐
             List<StrategyRecomd> list = recommentService.getList(limit,page);
             List<StrategyDTO> result = new ArrayList<>();
             for(int i=0;i<list.size();i++){
                 StrategyDTO strategyDTO = new StrategyDTO();
                 BeanUtils.copyProperties(list.get(i),strategyDTO);
                 User user = userMapper.selectByPrimaryKey(list.get(i).getUserId());
                 strategyDTO.setUserHeadImg(user.getImgUrl());
                 strategyDTO.setUserName(user.getName());
                 Number hotNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_HOT);
                 if(!Objects.isNull(hotNum)){
                     strategyDTO.setViewNum(hotNum.intValue());
                 }else{
                     strategyDTO.setViewNum(0);
                 }
                 Number colNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_COL_NUM);
                 if(!Objects.isNull(colNum)){
                     strategyDTO.setCollectnum(colNum.intValue());
                 }else{
                     strategyDTO.setCollectnum(0);
                 }
                 Number comNum = redisService.getViewNum(list.get(i).getId(), CollectionKey.ESSAY_KEY_COM_NUM);
                 if(!Objects.isNull(colNum)){
                     strategyDTO.setCommentnum(comNum.intValue());
                 }else{
                     strategyDTO.setCommentnum(0);
                 }
                 result.add(strategyDTO);
             }
             return new TableResultDTO<>(200, "", page+1, result);
         }
        return new TableResultDTO<>(500, "参数错误", 0, null);
    }

    /**
     * 获取热度前十的攻略
     * @return
     */
    @PostMapping("/hotstra-list")
    @ResponseBody
    public Response<List<Strategy>> topRecommendTenList() {
        List<Integer> listId = redisService.getTopNum(Constants.ESSAY_HOT_NAME);
        List<Strategy> result = new ArrayList<>();
        if(listId != null && listId.size()>0){
            listId.forEach(id->result.add(strategyService.selectStrategyById(id)));
        }
        return Response.success(result);
    }
    @PostMapping("/around_strategy")
    @ResponseBody
    public Response<List<Strategy>> aroundStrategy(HttpSession session) {
        String address = null;
        if(session.getAttribute("city") != null){
            address = (String)session.getAttribute("city") ;
        }else{
            address = "北京";
        }
        return Response.success(strategyService.getList(10,1,address));
    }
    @PostMapping("/besnew_strategy")
    @ResponseBody
    public Response<List<Strategy>> bestNewStrategy() {
        return Response.success(strategyService.getList(10,1,null));
    }
}
