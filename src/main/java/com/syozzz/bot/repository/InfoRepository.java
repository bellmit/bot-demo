package com.syozzz.bot.repository;

import com.syozzz.bot.repository.model.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * info repo
 * @author syozzz
 * @version 1.0
 * @date 2020-10-19
 */
public interface InfoRepository extends JpaRepository<Info, Long> {


    /**
     * 获取组内所有信息
     * @param groupId 群ID
     * @return 列表信息
     */
    List<Info> findAllByGroupIdIs(Long groupId);

    /**
     * 删除组内所有信息
     * @param groupId 群ID
     */
    void deleteAllByGroupIdIs(Long groupId);

    /**
     * 根据名称获取组内信息
     * @param groupId 群ID
     * @param name 名称
     * @return 信息实体
     */
    Info findByGroupIdIsAndNameIsLike(Long groupId, String name);

}
