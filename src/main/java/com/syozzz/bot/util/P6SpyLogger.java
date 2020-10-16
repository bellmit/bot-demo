package com.syozzz.bot.util;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import lombok.NoArgsConstructor;

/**
 * @author syozzz
 * @version 1.0
 * @date 2020-10-16
 */
@NoArgsConstructor
public class P6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return !"".equals(sql.trim()) ? "took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
    }
}
