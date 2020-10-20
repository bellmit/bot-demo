package com.syozzz.bot.service;


import com.syozzz.bot.repository.model.Info;

import java.util.List;

/**
 * info 服务
 * @author syozzz
 * @version 1.0
 * @date 2020-10-17
 */
public interface IInfoService {

    /**
     * 保存信息
     * @param info 信息
     */
    void save(Info info);

    /**
     * 获取组内所有信息
     * @param groupId 群ID
     * @return 列表信息
     */
    List<Info> findAllByGroupIdIs(Long groupId);

    /**
     * 清除组内所有信息
     * @param groupId 组ID
     */
    void clearGroupInfos(Long groupId);

    /**
     * 根据ID删除
     * @param id 信息ID
     */
    void deleteById(Long id);

    /**
     * 根据名称获取组内信息
     * @param groupId 群ID
     * @param name 名称
     * @return 信息实体
     */
    Info findByGroupIdIsAndNameIsLike(Long groupId, String name);

}
