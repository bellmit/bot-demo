package com.syozzz.bot.client;

import com.syozzz.bot.annotation.FeignApi;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

/**
 * 天气API
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@FeignApi(host = "http://www.weather.com.cn")
public interface WeatherClient {

    /**
     * 免费的获取天气API
     * @param cityId 城市ID
     * @return 天气信息
     */
    @RequestLine("GET /data/cityinfo/{id}.html")
    Map<String, Object> weather(@Param("id") String cityId);

}
