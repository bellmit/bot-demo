package com.syozzz.bot.handler;

import net.mamoe.mirai.message.GroupMessageEvent;

import java.net.MalformedURLException;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
public interface TaskHandler {

    void handle(String msg, GroupMessageEvent event);

}



