package com.syozzz.bot.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 将类注册为 EventHandler
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface HandlerChain {

    /**
     * 注册的 bean 名称
     * @return 名称
     */
    @AliasFor(annotation = Component.class)
    String value() default "";

    /**
     * 执行 handler 的用户键入命令
     * @return 命令数组
     */
    String[] cmds();
}
