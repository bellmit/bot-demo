package com.syozzz.bot.client;

import com.syozzz.bot.annotation.FeignApi;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Map;

/**
 * 涩图API
 * @author syozzz
 * @version 1.0
 * @date 2020-10-15
 */
@FeignApi(host = "https://api.lolicon.app")
public interface SetuClient {

    /**
     * 随机获取涩图接口
     * @param queryMap 查询参数
     * @return json
     */
    @RequestLine("GET /setu/")
    Map<String, Object> setu(@QueryMap Map<String, Object> queryMap);

}
