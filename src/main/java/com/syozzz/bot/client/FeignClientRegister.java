package com.syozzz.bot.client;

import com.syozzz.bot.annotation.FeignApi;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Feign 服务注册
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@Component
@Slf4j
public class FeignClientRegister implements BeanFactoryPostProcessor {

    private static final String SCAN_PATH = "com.syozzz.bot.client";


    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory beanFactory) {
        ClassInfoList feignClassInfo = scan();
        if(feignClassInfo == null) {
            return;
        }
        log.info("加载 Feign client, total:{}", feignClassInfo.size());
        feignClassInfo.forEach(classInfo -> {
            Feign.Builder builder = Feign.builder()
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .client(new OkHttpClient())
                    .options(new Request.Options(2000, 3500))
                    .retryer(new Retryer.Default(5000, 5000, 3))
                    .logger(new Logger.JavaLogger().appendToFile("http.log"))
                    .logLevel(Logger.Level.FULL);
            beanFactory.registerSingleton(classInfo.getName(),
                    builder.target(classInfo.loadClass(),
                            classInfo.loadClass().getAnnotation(FeignApi.class).host()));
        });
    }

    private ClassInfoList scan() {
        try(ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages(SCAN_PATH).scan()){
            return scanResult.getAllClasses()
                    .filter(classInfo -> classInfo.hasAnnotation("com.syozzz.bot.annotation.FeignApi"));
        } catch (IOException e) {
            log.error("扫描 Feign client 异常", e);
        }
        return null;
    }
}
