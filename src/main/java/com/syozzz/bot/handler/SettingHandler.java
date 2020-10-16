package com.syozzz.bot.handler;

import com.syozzz.bot.annotation.HandlerChain;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@HandlerChain(value = "settingHandler", cmds = {"T:设置"})
public class SettingHandler implements TaskHandler {


    @Override
    public void handle(String msg, GroupMessageEvent event) {
        event.getGroup().sendMessage("设置尼玛呢, 写代码不要时间的啊？爬");
    }
}
