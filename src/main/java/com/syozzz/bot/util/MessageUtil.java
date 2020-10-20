package com.syozzz.bot.util;

import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.repository.model.Info;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;

import java.util.ArrayList;
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

    public static void sendMultiMsg(Group group, List<String> data) {
        String msg = StrUtil.join("\n", data);
        group.sendMessage(msg);
    }

    public static void sendInfos(Group group, List<Info> infos) {
        List<String> msg = new ArrayList<>(infos.size());
        infos.forEach(info -> msg.add(info.toString()));
        sendMultiMsg(group, msg);
    }
}
