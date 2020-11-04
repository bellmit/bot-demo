package com.syozzz.bot.service;

import com.syozzz.bot.task.TaskProp;

import java.util.Date;

/**
 * 定时任务服务
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
public interface ITimerService {

    /**
     * 添加 job
     * @param task 执行的任务
     * @return 下一次执行的时间
     */
    Date addJob(TaskProp task);



}
