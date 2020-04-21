package com.guo.traveldemo.web.service;

import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.result.StrategyDTO;
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
}
