package com.hallo.fw.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * 字符串枚举类型处理器抽象基类
 * 
 * @author zhangxiaofeng
 * @create 2025年10月02日
 */
public abstract class AbstractStringEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, getValue(parameter));
  }

  @Override
  public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
    String value = rs.getString(columnName);
    return fromValue(value);
  }

  @Override
  public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    String value = rs.getString(columnIndex);
    return fromValue(value);
  }

  @Override
  public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    String value = cs.getString(columnIndex);
    return fromValue(value);
  }

  /**
   * 从枚举实例获取字符串值
   * 
   * @param enumValue 枚举实例
   * @return 字符串值
   */
  protected abstract String getValue(E enumValue);

  /**
   * 从字符串值获取枚举实例
   * 
   * @param stringValue 字符串值
   * @return 枚举实例
   */
  protected abstract E fromValue(String stringValue);

}