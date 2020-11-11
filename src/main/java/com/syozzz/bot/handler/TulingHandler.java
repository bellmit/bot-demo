package com.syozzz.bot.handler;

import com.syozzz.bot.client.TulingClient;
import com.syozzz.bot.entity.TulingQuery;
import com.syozzz.bot.util.MessageUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-11-11
 */
@Component
public class TulingHandler {

    @Autowired
    private TulingClient client;

    public void handle(String msg, GroupMessageEvent event) {
        msg = msg.replaceAll("@.+\\s", "");
        Map<String, Object> result = client.query(new TulingQuery(msg));
        if (result != null && result.containsKey("results")) {
            List<Map<String, Object>> res = (List<Map<String, Object>>) result.get("results");
            if (res.size() > 0) {
                MessageUtil.sendMultiMsg(event.getGroup(), handleResult(res));
            } else {
                err(event.getGroup());
            }
        } else {
            err(event.getGroup());
        }
    }

    private void err(Group group) {
        group.sendMessage("人家好像出了点问题呢~");
    }

    private List<String> handleResult(List<Map<String, Object>> res) {
        List<String> result = new ArrayList<>();
        res.forEach(stringObjectMap -> {
            Map<String, Object> values = (Map<String, Object>) stringObjectMap.get("values");
            result.add((String) values.get("text"));
        });
        return result;
    }
}
