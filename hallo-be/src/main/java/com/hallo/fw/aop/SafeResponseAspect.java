package com.hallo.fw.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hallo.api.response.HttpResponse;
import com.hallo.fw.exception.BizException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * SafeResponse注解的切面处理类
 */
@Slf4j
@Aspect
@Component
public class SafeResponseAspect {

  /**
   * 环绕通知处理带有SafeResponse注解的方法
   */
  @Around("@annotation(com.hallo.fw.annotation.SafeResponse) || @within(com.hallo.fw.annotation.SafeResponse)")
  public Object handleSafeResponseException(ProceedingJoinPoint joinPoint) throws Throwable {
    HttpServletRequest request = getCurrentRequest();
    String requestURI = request != null ? request.getRequestURI() : "unknown";
    String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

    try {
      // 执行目标方法
      Object result = joinPoint.proceed();

      log.info("请求处理成功: {} - {}", requestURI, method);

      return result;
    } catch (BizException e) {
      // 处理自定义异常
      log.error("业务异常: {} - {} - {}", requestURI, method, e.getMessage());
      return HttpResponse.error(e.getMessage());
    } catch (Exception e) {
      // 处理其他异常
      log.error("系统异常: {} - {} - {}", requestURI, method, e.getMessage(), e);
      return HttpResponse.error("系统异常，请联系管理员");
    }
  }

  /**
   * 获取当前请求对象
   */
  private HttpServletRequest getCurrentRequest() {
    try {
      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      return attributes != null ? attributes.getRequest() : null;
    } catch (Exception e) {
      return null;
    }
  }
}