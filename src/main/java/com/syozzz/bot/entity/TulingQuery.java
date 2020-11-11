package com.syozzz.bot.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-11-10
 */
@Data
public class TulingQuery {

    private Integer reqType = 0;

    private Map<String, Object> perception;

    private Map<String, Object> userInfo;

    public TulingQuery(String text) {
        perception = new HashMap<>();
        userInfo = new HashMap<>();

        Map<String, Object> params = new HashMap<>(1);
        params.put("text", text);
        perception.put("inputText", params);

        userInfo.put("apiKey", "1d20a730cff9460988a4fe20700e029c");
        userInfo.put("userId", "676527");
    }
}
