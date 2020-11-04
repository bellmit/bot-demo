package com.syozzz.bot.exception;

/**
 * 不支持的发送者类型
 * @author syozzz
 * @version 1.0
 * @date 2020-11-03
 */
public class UnsupportedSenderTypeException extends RuntimeException {

    private static final long serialVersionUID = 805180354649222690L;

    public UnsupportedSenderTypeException() {
        super();
    }

    public UnsupportedSenderTypeException(String message) {
        super(message);
    }

    public UnsupportedSenderTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedSenderTypeException(Throwable cause) {
        super(cause);
    }

}
