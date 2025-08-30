package com.hallo.fw.exception;

/**
 * 
 * @author
 * @date
 */
public class BizException extends RuntimeException {
    public BizException(String msg) {
        super(msg);
    }

    public static void throwException(String msg, Object... args) {
        String message = String.format(msg, args);
        throw new BizException(message);
    }
}
