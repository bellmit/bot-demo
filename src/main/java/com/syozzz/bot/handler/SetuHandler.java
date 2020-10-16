package com.syozzz.bot.handler;

import com.syozzz.bot.annotation.HandlerChain;
import com.syozzz.bot.client.SetuClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@Slf4j
@HandlerChain(value = "seTuHandler", cmds = {"色图", "涩图", "好看的", "好康的", "你懂的"})
public class SetuHandler implements TaskHandler {

    @Autowired
    private SetuClient client;

    @Override
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void handle(String msg, GroupMessageEvent event) {
        Map<String, Object> para = new HashMap<>(2);
        para.put("r18", "1");
        para.put("size1200", true);
        Object imageUrl = ((List<Map<String, Object>>)(client.setu(para).get("data"))).get(0).get("url");
        if (imageUrl != null) {
            log.info("获取图片成功: {}", imageUrl);
            URL url = new URL((String) imageUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //模拟器浏览器获取图片 不然 403
            conn.setRequestProperty("referer", "no referrer");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            conn.setRequestMethod("GET");
            //读取非常慢 后续改成自己的图床
            Image image = event.getGroup().uploadImage(conn.getInputStream());
            event.getGroup().sendMessage(image);
        } else {
            event.getGroup().sendMessage("涩图接口异常...");
        }
    }

}
