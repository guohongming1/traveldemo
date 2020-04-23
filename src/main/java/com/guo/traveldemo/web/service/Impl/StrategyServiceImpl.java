package com.guo.traveldemo.web.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guo.traveldemo.constants.Constants;
import com.guo.traveldemo.result.CodeMsg;
import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.result.StrategyDTO;
import com.guo.traveldemo.web.mapper.*;
import com.guo.traveldemo.web.pojo.Route;
import com.guo.traveldemo.web.pojo.Strategy;
import com.guo.traveldemo.web.pojo.StrategyDetail;
import com.guo.traveldemo.web.pojo.TravelTable;
import com.guo.traveldemo.web.service.StrategyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/12
 */
@Service
public class StrategyServiceImpl implements StrategyService {

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private TravelTableMapper travelTableMapper;

    @Autowired
    private RouteMapper routeMapper;

    @Autowired
    private StrategyDetailMapper strategyDetailMapper;
    @Autowired
    private CollectMapper collectMapper;

    /**
     * 发表游记
     * @param strategy
     * @param travelTable
     * @param route
     * @param content
     * @return
     */
    @Deprecated
    @Override
    @Transactional
    public Response<String> createStrategy(Strategy strategy, TravelTable travelTable, String route, String content){
        if (travelTableMapper.insertSelective(travelTable)>0){
             StrategyDetail strategyDetail = new StrategyDetail();
             strategyDetail.setContent(content);
             strategyDetail.setTableId(travelTable.getId());
            if(route.length() > 1){
                Route route1 = new Route();
                route1.setContent(route);
                routeMapper.insertSelective(route1);
                strategyDetail.setRouteId(route1.getId());
                strategyDetailMapper.insertSelective(strategyDetail);
                strategy.setDetailId(strategyDetail.getId());
                strategyMapper.insertSelective(strategy);
            }
        }
        return Response.success("发表成功");
    }

    /**
     * 更新攻略或者创建攻略
     * @param strategy
     * @param travelTable
     * @param route
     * @param content
     * @return
     */
    @Override
    @Transactional
    public Response<String> updateStrategy(Strategy strategy, TravelTable travelTable, String route, String content){
        // 查询用户是否有未发表的攻略
        QueryWrapper<Strategy> query = new QueryWrapper<>();
        query.eq("user_id",strategy.getUserId());
        query.eq("push_flag", Constants.PUSH_NO);
        Strategy recod = strategyMapper.selectOne(query);
        if(Objects.isNull(recod)){
            if (travelTableMapper.insertSelective(travelTable)>0){
                StrategyDetail strategyDetail = new StrategyDetail();
                strategyDetail.setContent(content);
                strategyDetail.setTableId(travelTable.getId());
                if(route.length() > 1){
                    Route route1 = new Route();
                    route1.setContent(route);
                    routeMapper.insertSelective(route1);
                    strategyDetail.setRouteId(route1.getId());
                    strategyDetailMapper.insertSelective(strategyDetail);
                    strategy.setDetailId(strategyDetail.getId());
                    strategyMapper.insertSelective(strategy);
                }
            }
        }else{
            // 执行更新
            strategy.setDetailId(recod.getDetailId());
            strategy.setId(recod.getId());
            strategyMapper.updateByPrimaryKey(strategy);
            StrategyDetail detail = strategyDetailMapper.selectByPrimaryKey(recod.getDetailId());
            detail.setContent(content);
            strategyDetailMapper.updateByPrimaryKeySelective(detail);
            Route routeT = routeMapper.selectByPrimaryKey(detail.getRouteId());
            routeT.setContent(route);
            TravelTable travelTable1 = travelTableMapper.selectByPrimaryKey(detail.getTableId());
            travelTable.setId(travelTable1.getId());
            travelTable.setStraDeId(travelTable1.getStraDeId());
            travelTableMapper.updateByPrimaryKey(travelTable);
            routeMapper.updateByPrimaryKey(routeT);
        }
        return Response.success("成功");
    }

    /**
     * 根据id获取攻略
     * @param id
     * @return
     */
    @Override
    public Strategy selectStrategyById(int id) {
        return strategyMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页获取攻略
     * @param limit
     * @param page
     * @return
     */
    @Override
    public List<Strategy> getList(int limit, int page,String address) {
        Page<Strategy> pageHelper = new Page<>();
        pageHelper.setCurrent(page);
        pageHelper.setSize(limit);
        IPage<Strategy> pageVo = strategyMapper.selectPageVo(pageHelper,address);
        return pageVo.getRecords();
    }

    /**
     * 获取未发表攻略
     * @param userId
     * @return
     */
    public Response<StrategyDTO> pullStrategy(int userId){
        QueryWrapper<Strategy> query = new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("push_flag", Constants.PUSH_NO);
        Strategy strategy = strategyMapper.selectOne(query);
        if(null == strategy){
            return Response.fail(CodeMsg.FAIL);
        }
        StrategyDetail strategyDetail = null;
        if(strategy.getDetailId() != null){
            strategyDetail = strategyDetailMapper.selectByPrimaryKey(strategy.getDetailId());
        }
        Route route=null;
        TravelTable travelTable=null;
        if(strategyDetail != null){
            route = routeMapper.selectByPrimaryKey(strategyDetail.getRouteId());
            travelTable = travelTableMapper.selectByPrimaryKey(strategyDetail.getTableId());
        }
        StrategyDTO result = new StrategyDTO();
        BeanUtils.copyProperties(strategy,result,"date");
        result.setRoute(route.getContent());
        result.setContent(strategyDetail.getContent());
        result.setDays(travelTable.getDays());
        result.setAddress(travelTable.getAddress());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        result.setDate(sf.format(travelTable.getDate()));
        result.setMoney(travelTable.getFee());
        result.setPeople(travelTable.getPeople());
        result.setAdvice(travelTable.getAdvice());
        return Response.success(result);
    }
    public StrategyDetail getDetailById(int detailId){
        return strategyDetailMapper.selectByPrimaryKey(detailId);
    }

    @Override
    public TravelTable getTavelTableById(int tableId) {
        return travelTableMapper.selectByPrimaryKey(tableId);
    }

    @Override
    public Route getRouteById(int routeId) {
        return routeMapper.selectByPrimaryKey(routeId);
    }

    @Override
    public List<Strategy> getStrategyByUserId(int id){
        QueryWrapper<Strategy> query = new QueryWrapper<>();
        query.eq("user_id",id);
        return strategyMapper.selectList(query);
    }

}
