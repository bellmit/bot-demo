package com.syozzz.bot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.entity.Constant;
import com.syozzz.bot.repository.TaskRepository;
import com.syozzz.bot.service.ITimerService;
import com.syozzz.bot.task.QuartzJob;
import com.syozzz.bot.task.TaskProp;
import lombok.SneakyThrows;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 定时任务服务实现
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
@Service
public class TimerServiceImpl implements ITimerService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final TaskRepository taskRepository;

    private static final String JOB_KEY_PREFIX = "TASK_";

    @Autowired
    public TimerServiceImpl(SchedulerFactoryBean schedulerFactoryBean, TaskRepository taskRepository) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.taskRepository = taskRepository;
    }

    private String getJobKey(String taskId) {
        return StrUtil.format("{}{}", JOB_KEY_PREFIX, taskId);
    }

    private TriggerKey getTriggerKey(String taskId) {
        return TriggerKey.triggerKey(getJobKey(taskId));
    }

    /**
     * 获取调度器
     * @return 调度器
     */
    private Scheduler getScheduler(){
        return schedulerFactoryBean.getScheduler();
    }

    @Override
    @SneakyThrows(SchedulerException.class)
    public Date addJob(TaskProp task) {
        // 1.获取调度器 Scheduler
        Scheduler scheduler = getScheduler();
        // 2.定义 jobDetail(构建job信息)
        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class).withIdentity(getJobKey(task.getTaskId())).build();
        // 3.定义trigger（按新的cronExpression表达式构建一个新的trigger）
        // 不触发立即执行，等待下次Cron触发频率到达时刻开始按照Cron频率依次执行  withMisfireHandlingInstructionDoNothing
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron()).withMisfireHandlingInstructionDoNothing();
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(task.getTaskId())).withSchedule(cronScheduleBuilder).build();
        jobDetail.getJobDataMap().put(Constant.TIMER_JOB_PARAM_KEY, task);
        // 4.执行
        Date startTime = scheduler.scheduleJob(jobDetail,trigger);
        //保存任务详情
        taskRepository.save(task);
        return startTime;
    }
}
