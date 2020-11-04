package com.syozzz.bot.util;

import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    @NonNull
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    public static <T>T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    public static <T>T getBean(String name,Class<T> requiredType){
        return applicationContext.getBean(name,requiredType);
    }

    public static boolean containsBean(String name){
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name){
        return applicationContext.getType(name);
    }
}
