package com.green.acamatch.config.exception;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class StringToListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        // List를 문자열로 변환하여 데이터베이스에 저장 (예: 콤마로 구분된 문자열)
        ps.setString(i, String.join(",", parameter));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // VARCHAR 값을 List로 변환
        String value = rs.getString(columnName);
        return value != null ? Arrays.asList(value.split(",")) : null;
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // VARCHAR 값을 List로 변환
        String value = rs.getString(columnIndex);
        return value != null ? Arrays.asList(value.split(",")) : null;
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // CallableStatement 지원
        String value = cs.getString(columnIndex);
        return value != null ? Arrays.asList(value.split(",")) : null;
    }
}