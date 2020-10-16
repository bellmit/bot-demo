package com.syozzz.bot.util;

import cn.hutool.core.util.StrUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.List;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
public class MessageUtil {

    private MessageUtil() {}

    public static String msgToString(MessageChain chain) {
        return chain.contentToString();
    }

    public static void sendMultiMsg(Group group, List<String> weatherInfo) {
        String msg = StrUtil.join("\n", weatherInfo);
        group.sendMessage(msg);
    }

}
