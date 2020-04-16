package com.guo.traveldemo.web.service;

import com.guo.traveldemo.result.Response;
import com.guo.traveldemo.web.pojo.TravelGroup;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/4/16
 */
public interface GroupService {
    Response<String> createGroup(TravelGroup group);
}
