package com.guo.traveldemo.web.service;

import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.dto.HotMap;
import com.guo.traveldemo.web.dto.StrategyDTO;
import com.guo.traveldemo.web.pojo.StrategyRecomd;

import java.util.List;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/20
 */
public interface RecommentService {

   public List<StrategyRecomd> getList(int limit, int page);

   List<StrategyDTO> top(List<Integer> recommends, boolean topTen);
}
