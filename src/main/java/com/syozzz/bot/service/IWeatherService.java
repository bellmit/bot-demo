package com.syozzz.bot.service;

import java.util.Collection;
import java.util.List;

/**
 * 天气服务
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
public interface IWeatherService {

    /**
     * 群注册每日天气通知
     * @param gId 群ID
     */
    void registerGroupWeatherNotify(Long gId);

    /**
     * 取消天气通知
     * @param gId 群ID
     */
    void cancleWetherNotify(Long gId);

    /**
     * 获取所有的注册天气通知服务的group
     * @return 组ID集合
     */
    Collection<Long> getAllRegisterGroups();

    /**
     * 识别 group 是否已注册通知服务
     * @param gId 组ID
     * @return 结果
     */
    Boolean isAlreadyRegister(Long gId);

    /**
     * 群添加天气通知的城市
     * @param gId 群ID
     * @param cityIds 城市ID
     */
    void addNotifyCity(Long gId, Long... cityIds);

    /**
     * 移除对应群组天气通知的城市
     * @param gId 群ID
     * @param cityIds 城市ID
     */
    void removeNotifyCity(Long gId, Long... cityIds);

    /**
     * 删除通知城市数据
     * @param gId 群ID
     */
    void deleteNotifyCity(Long gId);

    /**
     * 获取群组关注的所有城市
     * @param gId 群ID
     * @return 城市
     */
    Collection<Long> getAllCitysOfGroup(Long gId);

    /**
     * 根据城市名称获取城市ID
     * @param name 名称
     * @return id
     */
    Long findCityIdByCityName(String name);

    /**
     * 获取群组关注的城市的所有天气情况
     * @param gid 群组ID
     * @return 天气信息列表
     */
    List<String> getWeatherInfoByGroup(Long gid);

    /**
     * 根据城市名称
     * @param name 城市名称
     * @return 天气详情
     */
    List<String> getWeatherInfoByCityName(String name);
}
