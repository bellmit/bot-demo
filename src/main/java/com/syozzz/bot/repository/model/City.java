package com.syozzz.bot.repository.model;

import lombok.Data;

import javax.persistence.*;

/**
 * 城市编码对应表
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@Entity
@Table(name = "city")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long cid;

    @Column
    private String cname;
}
