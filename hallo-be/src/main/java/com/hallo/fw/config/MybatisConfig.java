package com.hallo.fw.config;

import javax.sql.DataSource;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * mybatis 配置类
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日 14:48:45
 */
@Configuration
@MapperScan("com.hallo.**.mapper")
public class MybatisConfig {
  @Autowired
  private DataSource dataSource;

  /**
   * 配置 SqlSessionFactory
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(dataSource);

    // 设置 MyBatis 配置
    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();

    // 开启驼峰命名转换
    configuration.setMapUnderscoreToCamelCase(true);

    // 开启二级缓存
    configuration.setCacheEnabled(true);

    // 开启延迟加载
    configuration.setLazyLoadingEnabled(true);
    configuration.setAggressiveLazyLoading(false);

    // 设置执行器类型
    configuration.setDefaultExecutorType(org.apache.ibatis.session.ExecutorType.REUSE);

    // 设置超时时间
    configuration.setDefaultStatementTimeout(30);

    // 注册自定义 TypeHandler
    registerTypeHandlers(configuration);

    factoryBean.setConfiguration(configuration);

    // 设置 mapper 文件位置
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    factoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/**/*.xml"));

    return factoryBean.getObject();
  }

  /**
   * 注册自定义 TypeHandler
   */
  private void registerTypeHandlers(org.apache.ibatis.session.Configuration configuration) {
    // 注册 JSON TypeHandler (如果需要)
    // configuration.getTypeHandlerRegistry().register(JsonTypeHandler.class);

    // 自动扫描并注册 com.hallo.fw.typehandler 包下的所有 TypeHandler
    configuration.getTypeHandlerRegistry().register("com.hallo.fw.typehandler");

    // 注册日期时间 TypeHandler
    configuration.getTypeHandlerRegistry().register(java.time.LocalDateTime.class,
        org.apache.ibatis.type.LocalDateTimeTypeHandler.class);
    configuration.getTypeHandlerRegistry().register(java.time.LocalDate.class,
        org.apache.ibatis.type.LocalDateTypeHandler.class);
    configuration.getTypeHandlerRegistry().register(java.time.LocalTime.class,
        org.apache.ibatis.type.LocalTimeTypeHandler.class);
  }
}