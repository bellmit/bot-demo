package com.syozzz.bot.repository.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 保存信息
 * @author syozzz
 * @version 1.0
 * @date 2020-10-19
 */
@Entity
@Table(name = "info")
@Data
@NoArgsConstructor
public class Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private Long groupId;

    @Column
    private String name;

    @Column
    private String content;

    public Info(Long groupId, String name, String content) {
        this.groupId = groupId;
        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return StrUtil.format("id:[{}], name:[{}], content:[{}]", id, name, content);
    }
}
