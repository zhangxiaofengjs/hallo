package com.hallo.fw.exception;

/**
 * 业务异常类
 * 
 * @author
 * @date
 */
public class BizException extends RuntimeException {
  public BizException(String msg) {
    super(msg);
  }

  /**
   * 抛出业务异常并返回指定类型，用于避免编译器要求的return语句
   * 
   * @param <T>  返回值类型
   * @param msg  异常消息
   * @param args 消息参数
   * @return 无实际返回值，仅为满足编译器要求
   * @throws BizException 业务异常
   */
  public static <T> T throwException(String msg, Object... args) {
    String message = String.format(msg, args);
    throw new BizException(message);
  }
}