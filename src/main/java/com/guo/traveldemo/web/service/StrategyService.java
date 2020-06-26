package com.guo.traveldemo.web.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.result.StrategyDTO;
import com.guo.traveldemo.web.pojo.Route;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyDetail;
import com.guo.traveldemo.web.pojo.TravelTable;

import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
public interface StrategyService {

    public Response<String> createStrategy(Strategy strategy, TravelTable travelTable, String route, String content);
    Response<StrategyDTO> pullStrategy(int userId);
    Response<String> updateStrategy(Strategy strategy, TravelTable travelTable, String route, String content);
    Strategy selectStrategyById(int id);
    List<Strategy> getList(int limit,int page,String address);
    List<Strategy> adminGetList(int limit,int page,String title);
    int getCount();
    StrategyDetail getDetailById(int detailId);
    TravelTable getTavelTableById(int tableId);
    Route getRouteById(int routeId);
    List<Strategy> getStrategyByUserId(int id);
    List<Strategy> queryStrategy(QueryWrapper<Strategy> query);
    int updateStrategy(Strategy strategy);
    int delBatchStrategy(List<Integer> ids);
}
