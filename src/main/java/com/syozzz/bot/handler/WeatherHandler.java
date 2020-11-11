package com.syozzz.bot.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.annotation.HandlerChain;
import com.syozzz.bot.service.IWeatherService;
import com.syozzz.bot.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessageEvent;
import net.mamoe.mirai.message.data.AtAll;
import net.mamoe.mirai.message.data.MessageUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 天气 handler
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@Slf4j
@HandlerChain(value = "weatherHandler", cmds = {"help:weather", "weather", "播报天气", "开启天气预报", "T:取消天气预报", "TQ:"})
public class WeatherHandler implements TaskHandler {

    private final IWeatherService weatherService;

    private final TaskDispatcher taskDispatcher;

    @Autowired
    public WeatherHandler(IWeatherService weatherService, TaskDispatcher taskDispatcher) {
        this.weatherService = weatherService;
        this.taskDispatcher = taskDispatcher;
    }

    private static final String CMD_WEATHER_OPEN = "开启天气预报";
    private static final String CMD_WEATHER_TIPS = "help:weather";
    private static final String CMD_WEATHER_CLOSE = "T:取消天气预报";
    private static final String CMD_WEATHER_CITY_PREFIX = "TQ:";
    private static final String CMD_WEATHER_CITY_ADD = "TQ:ADD:";
    private static final String CMD_WEATHER_CITY_DEL = "TQ:DEL:";
    private static final String CMD_WEATHER_CITY_SEP = ",";

    @Override
    public void handle(String msg, GroupMessageEvent event) {
        Long gId = event.getGroup().getId();
        if (CMD_WEATHER_OPEN.equals(msg)) {
            handleWeatherOpen(event, gId);
        } else if (CMD_WEATHER_TIPS.equals(msg)) {
            MessageUtil.sendMultiMsg(event.getGroup(), CollUtil.newArrayList(
                    "开启天气预报: [开启天气预报]",
                    "取消天气预报: [T:取消天气预报]",
                    "添加关注城市: [TQ:ADD:XXX,XXX]",
                    "删除关注城市: [TQ:DEL:XXX,XXX]",
                    "播报天气: [播报天气 OR weather]",
                    "展示天气相关命令: [help:weather]"
            ));
        } else if (CMD_WEATHER_CLOSE.equals(msg)) {
            handleWeatherClose(event, gId);
        } else if (msg.startsWith(CMD_WEATHER_CITY_PREFIX)) {
            if (msg.startsWith(CMD_WEATHER_CITY_ADD)) {
                handleCityAdd(msg, event, gId);
            } else if (msg.startsWith(CMD_WEATHER_CITY_DEL)) {
                handleCityDel(msg, event, gId);
            }
        } else {
            if (Boolean.FALSE.equals(weatherService.isAlreadyRegister(gId))) {
                event.getGroup().sendMessage("本群未开通天气服务功能.");
                return;
            }
            //获取本群所有关注的 city
            Collection<Long> citys = weatherService.getAllCitysOfGroup(gId);
            if (citys.isEmpty()) {
                event.getGroup().sendMessage("本群尚无关注城市.");
            } else {
                List<String> infos = weatherService.getWeatherInfoByGroup(gId);
                MessageUtil.sendMultiMsg(event.getGroup(), infos);
            }
        }
    }

    private void handleCityDel(String msg, GroupMessageEvent event, Long gId) {
        String s = StrUtil.subAfter(msg, ":", true);
        if (StrUtil.isBlank(s)) {
            event.getGroup().sendMessage("cmd 错误。[TQ:DEL:xxx]");
            return;
        }
        List<Long> allCity = getCitys(s);
        allCity.forEach(aCity -> weatherService.removeNotifyCity(gId, aCity));
        event.getGroup().sendMessage("城市移除成功");
    }

    private void handleCityAdd(String msg, GroupMessageEvent event, Long gId) {
        String s = StrUtil.subAfter(msg, ":", true);
        if (StrUtil.isBlank(s)) {
            event.getGroup().sendMessage("cmd 错误。[TQ:ADD:xxx]");
            return;
        }
        List<Long> allCity = getCitys(s);
        weatherService.addNotifyCity(gId, allCity.toArray(new Long[allCity.size()]));
        event.getGroup().sendMessage("城市添加成功");
    }

    private void handleWeatherClose(GroupMessageEvent event, Long gId) {
        if (Boolean.TRUE.equals(weatherService.isAlreadyRegister(gId))) {
            weatherService.cancleWetherNotify(gId);
            weatherService.deleteNotifyCity(gId);
            event.getGroup().sendMessage("成功退订天气预报服务");
        } else {
            event.getGroup().sendMessage("本群未开通天气预报服务");
        }
    }

    private void handleWeatherOpen(GroupMessageEvent event, Long gId) {
        if (Boolean.TRUE.equals(weatherService.isAlreadyRegister(gId))) {
            event.getGroup().sendMessage("本群已开通天气预报服务, 操作无效。");
        } else {
            //注册通知
            weatherService.registerGroupWeatherNotify(gId);
            event.getGroup().sendMessage("开通天气预报服务, 将在每天早上7点半为您播报叫床和天气预报服务。\n回复 T:取消天气预报 取消服务。");
            event.getGroup().sendMessage("tips:\nTQ:ADD:城市名,城市名 设置您希望每日预报的城市。\nTQ:DEL:城市名 移除城市。\n未关注城市将不会播报天气哦~");
        }
    }

    @NotNull
    private List<Long> getCitys(String s) {
        List<Long> allCity = new ArrayList<>();
        for (String city : s.split(CMD_WEATHER_CITY_SEP)) {
            Long cid = weatherService.findCityIdByCityName(city);
            if (cid != null) {
                allCity.add(cid);
            }
        }
        return allCity;
    }

    @Scheduled(cron = "0 0 23 * * ?")
    private void sleepNotify() {
        log.info("执行晚安提醒...");
        Collection<Long> groups = weatherService.getAllRegisterGroups();
        for (Object g : groups) {
            long group = 0L;
            if (g instanceof Long) {
                group = (Long) g;
            } else if (g instanceof Integer) {
                group = ((Integer) g).longValue();
            }
            taskDispatcher
                    .getBot()
                    .getGroup(group)
                    .sendMessage(
                            MessageUtils.newChain("该睡觉了, 憨批们, 自觉点")
                                    .plus(AtAll.INSTANCE));
        }
    }

    @Scheduled(cron = "0 0 7 * * ?")
    private void morningNotify() {
        log.info("执行起床提醒...");
        Collection<Long> groups = weatherService.getAllRegisterGroups();
        for (Object g : groups) {
            long gid = 0L;
            if (g instanceof Long) {
                gid = (Long) g;
            } else if (g instanceof Integer) {
                gid = ((Integer) g).longValue();
            }
            Group group = taskDispatcher.getBot().getGroup(gid);
            group.sendMessage(
                    MessageUtils.newChain("早安, 打工人~ 该起来搬砖了")
                            .plus(AtAll.INSTANCE));
            Collection<Long> citys = weatherService.getAllCitysOfGroup(gid);
            if (!citys.isEmpty()) {
                List<String> infos = weatherService.getWeatherInfoByGroup(gid);
                MessageUtil.sendMultiMsg(group, infos);
            }
        }
    }

}
