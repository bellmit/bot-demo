package com.syozzz.bot.task;

import com.syozzz.bot.entity.Constant;
import com.syozzz.bot.exception.UnsupportedSenderTypeException;
import com.syozzz.bot.handler.TaskDispatcher;
import com.syozzz.bot.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 定时任务执行
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
@DisallowConcurrentExecution
@Slf4j
public class QuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        //获取任务执行参数
        TaskProp props = (TaskProp) jobExecutionContext.getMergedJobDataMap().get(Constant.TIMER_JOB_PARAM_KEY);
        switch (props.getType()) {
            case USER:
                userJobHandle(props.getId(), props.getSendMsg());
                break;
            case GROUP:
                groupJobHandle(props.getId(), props.getSendMsg());
                break;
            default:
                throw new UnsupportedSenderTypeException("unsupported type of senderType!");
        }
    }

    private Bot getBot() {
        return ((TaskDispatcher) SpringContextUtil.getBean("taskDispatcher")).getBot();
    }

    private void groupJobHandle(Long id, String sendMsg) {
        log.info("执行定时任务, id:{}, msg:{}, type:group", id, sendMsg);
        Bot bot = getBot();
        bot.getGroup(id).sendMessage(sendMsg);
    }

    private void userJobHandle(Long id, String sendMsg) {
        log.info("执行定时任务, id:{}, msg:{}, type:user", id, sendMsg);
        Bot bot = getBot();
        bot.getFriend(id).sendMessage(sendMsg);
    }

}
