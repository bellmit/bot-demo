package com.syozzz.bot.task;

import com.syozzz.bot.entity.SenderType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 任务配置
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
@Entity
@Table(
        name = "task",
        indexes = {
                @Index(name = "idx_bto_task_sid", columnList = "id")
        }
)
@Data
public class TaskProp {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String taskId;

    @Column
    private String cron;

    @Column
    private Long id;

    @Column
    private String sendMsg;

    @Column
    private Date createTime;

    @Column
    private SenderType type;

}
