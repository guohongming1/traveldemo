package com.guo.traveldemo.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guo.traveldemo.web.pojo.MsgConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface MsgConfigMapper extends BaseMapper<MsgConfig> {
    int deleteByPrimaryKey(Integer id);

    int insert(MsgConfig record);

    int insertSelective(MsgConfig record);

    MsgConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MsgConfig record);

    int updateByPrimaryKey(MsgConfig record);

    @Select("select * from msg_config where user_id=#{id}")
    MsgConfig selectByUserId(Integer id);
}