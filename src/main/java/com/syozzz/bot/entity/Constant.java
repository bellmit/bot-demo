package com.syozzz.bot.entity;

import java.util.regex.Pattern;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
public class Constant {

    private Constant () {}

    public static final Integer SETU_DEFAULT_SIZE = 1;
    public static final Integer SETU_MAX_SIZE = 3;
    public static final Pattern SETU_NUM_PATTERN = Pattern.compile("\\d+");

}
