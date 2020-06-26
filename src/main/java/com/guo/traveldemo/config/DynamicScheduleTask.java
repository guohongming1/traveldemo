package com.guo.traveldemo.config;

import com.alibaba.druid.util.StringUtils;
import com.guo.traveldemo.web.mapper.CronMapper;
import com.guo.traveldemo.web.service.TriggerTaskService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 * @author 郭红明
 * @version 1.0
 * @Date: 2020/5/12
 */
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DynamicScheduleTask implements SchedulingConfigurer {
    @Autowired      //注入mapper
    CronMapper cronMapper;
    @Autowired
    private TriggerTaskService triggerTaskService;
    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> System.out.println("执行动态定时任务: " + LocalDateTime.now().toLocalTime()),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String cron = cronMapper.selectByPrimaryKey(1).getCron();
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron)) {
                        triggerTaskService.initTask();//执行定时任务
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }

}
