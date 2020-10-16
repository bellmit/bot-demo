package com.syozzz.bot.handler;

import com.syozzz.bot.annotation.HandlerChain;
import net.mamoe.mirai.message.GroupMessageEvent;

/**
 * 系统信息 handler
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@HandlerChain(value = "infoHandler", cmds = {"appInfo", "系统信息"})
public class SystemInfoHandler implements TaskHandler {


    private static final String INFO = "syo.qq.bot v0.1 © 2020 syozzz\npower by mirai";

    @Override
    public void handle(String msg, GroupMessageEvent event) {
        event.getGroup().sendMessage(INFO);
    }

}
