package com.syozzz.bot.handler;

import cn.hutool.core.util.StrUtil;
import com.syozzz.bot.annotation.HandlerChain;
import com.syozzz.bot.repository.model.Info;
import com.syozzz.bot.service.IInfoService;
import com.syozzz.bot.util.MessageUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.GroupMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

/**
 * 存储
 * @author syozzz
 * @version 1.0
 * @date 2020-10-17
 */
@HandlerChain(value = "infoHandler", cmds = {"INFO:", "I:"})
public class InfoHandler implements TaskHandler {

    private static final String CMD_SAVE_ADD = "INFO:ADD:";
    private static final String CMD_SAVE_DEL = "INFO:DEL:";
    private static final String CMD_SAVE_SHOW = "INFO:SHOW";
    private static final String CMD_SAVE_ONE = "I:";

    private final IInfoService infoService;

    @Autowired
    public InfoHandler(IInfoService saveService) {
        this.infoService = saveService;
    }

    @Override
    public void handle(String msg, GroupMessageEvent event) {
        if (msg.startsWith(CMD_SAVE_ADD)) {
            handleInfoAdd(msg, event);
        } else if (msg.equals(CMD_SAVE_SHOW)) {
            handleInfoShow(event.getGroup());
        }  else if (msg.startsWith(CMD_SAVE_ONE)) {
            handleInfoOne(msg, event.getGroup());
        }
    }

    private void handleInfoOne(String msg, Group group) {
        String name = StrUtil.subAfter(msg, ":", true);
        if (StrUtil.isBlank(name)) {
            group.sendMessage("cmd 错误。I:xxx");
        } else {
            Info info = infoService.findByGroupIdIsAndNameIsLike(group.getId(), name);
            if (Objects.isNull(info)) {
                group.sendMessage(StrUtil.format("未查询到[{}]", name));
            } else {
                group.sendMessage(info.getContent());
            }
        }
    }

    private void handleInfoShow(Group group) {
        List<Info> infos = infoService.findAllByGroupIdIs(group.getId());
        if (infos.isEmpty()) {
            group.sendMessage("本群尚无保存的共享信息.");
        } else {
            MessageUtil.sendInfos(group, infos);
        }
    }

    private void handleInfoAdd(String msg, GroupMessageEvent event) {
        Group group = event.getGroup();
        String s = StrUtil.subSuf(msg, CMD_SAVE_ADD.length());
        if (StrUtil.isBlank(s)) {
            group.sendMessage("cmd 错误。SAVE:ADD:xxxx:[内容]");
            return;
        }
        String key = StrUtil.subBefore(s, ":", false);
        String content = StrUtil.subAfter(s, ":", false);
        if (StrUtil.isBlank(key) || StrUtil.isBlank(content)) {
            group.sendMessage("cmd 错误。SAVE:ADD:xxxx:[内容]");
            return;
        }
        String value = StrUtil.unWrap(content, "[", "]");
        Info info = new Info(group.getId(), key, value);
        infoService.save(info);
        group.sendMessage("保存成功");
    }
}
