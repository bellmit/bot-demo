package com.syozzz.bot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注册 Feign 服务的扫描注解
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignApi {

    String host();

}
