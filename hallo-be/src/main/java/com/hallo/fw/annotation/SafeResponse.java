package com.hallo.fw.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 安全响应异常注解
 * 用于标记Controller方法，使其能够统一处理异常并返回标准格式的响应
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日 21:08:37
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeResponse {

}