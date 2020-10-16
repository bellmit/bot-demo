package com.syozzz.bot.handler;

import com.syozzz.bot.annotation.HandlerChain;
import com.syozzz.bot.handler.TaskHandler;
import com.syozzz.bot.util.MessageUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactoryJvm;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.Events;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.utils.BotConfiguration;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bot 任务分发
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@Component
public class TaskDispatcher {

    @Value("${bot.info.user}")
    private Long user;
    @Value("${bot.info.password}")
    private String password;
    @Autowired
    private List<TaskHandler> handlers;
    private Map<String[], TaskHandler> cache;

    private Bot bot;

    private void initCacheMap() {
        cache = new HashMap<>(handlers.size());
        handlers.forEach(taskHandler -> {
            Class<? extends TaskHandler> beanClazz = taskHandler.getClass();
            HandlerChain anno = beanClazz.getAnnotation(HandlerChain.class);
            String[] cmds = anno.cmds();
            cache.put(cmds, taskHandler);
        });
    }

    @PostConstruct
    public void init() {

        initCacheMap();

        bot = BotFactoryJvm.newBot(user, password, new BotConfiguration() {
            {
                //保存设备信息到文件
                fileBasedDeviceInfo("deviceInfo.json");
            }
        });

        bot.login();

        Events.registerEvents(bot, new SimpleListenerHost() {

            @EventHandler
            public ListeningStatus onGroupMessage(GroupMessageEvent event) {
                String msgString = MessageUtil.msgToString(event.getMessage());
                cache.forEach((strings, taskHandler) -> {
                    if (contains(strings, msgString)) {
                        taskHandler.handle(msgString, event);
                    }
                });
                //保持监听
                return ListeningStatus.LISTENING;
            }

            private boolean contains(String[] array, String target) {
                boolean result = false;
                for (String s : array) {
                    if (target.contains(s)) {
                        result = true;
                        break;
                    }
                }
                return result;
            }

            @Override
            public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
                throw new RuntimeException("在事件处理中发生异常", exception);
            }
        });
    }

    public Bot getBot() {
        return bot;
    }

}
