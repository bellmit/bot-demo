package com.syozzz.bot.client;

import com.syozzz.bot.annotation.FeignApi;
import com.syozzz.bot.entity.TulingQuery;
import feign.Headers;
import feign.RequestLine;

import java.util.Map;

/**
 * 图灵机器人 client
 * @author syozzz
 * @version 1.0
 * @date 2020-11-10
 */
@FeignApi(host = "http://openapi.tuling123.com")
public interface TulingClient {


    /**
     * 智能问答接口
     * @return 回答信息
     */
    @RequestLine("POST /openapi/api/v2")
    @Headers("Content-Type: application/json")
    Map<String, Object> query(TulingQuery query);

}
