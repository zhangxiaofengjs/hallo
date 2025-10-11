package com.hallo.fw.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * 安全配置类
 * 
 * @author zhangxiaofeng
 * @create 2025年10月11日 13:42:21
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable) // 禁用CSRF保护
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/**", "/ws/**").permitAll() // 允许所有/api路径的访问
            .anyRequest().authenticated() // 其他请求需要认证
        )
        .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 配置CORS
        .exceptionHandling(ex -> ex.authenticationEntryPoint(customAuthenticationEntryPoint)); // 配置认证入口点

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 允许所有来源
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的HTTP方法
    configuration.setAllowedHeaders(Arrays.asList("*")); // 允许所有头部
    configuration.setAllowCredentials(true); // 允许携带凭证

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", configuration); // 为/api路径配置CORS
    source.registerCorsConfiguration("/ws/**", configuration); // 为/ws路径配置CORS
    return source;
  }
}
