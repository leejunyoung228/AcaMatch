package com.green.acamatch.entity.attendance;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(AttendanceStatus.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AttendanceTypeHandler extends BaseTypeHandler<AttendanceStatus> { //MyBatis 이용
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, AttendanceStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue()); // Enum → 한글 값으로 저장
    }

    @Override
    public AttendanceStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return AttendanceStatus.fromValue(rs.getString(columnName)); // 한글 값 → Enum 변환
    }

    @Override
    public AttendanceStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return AttendanceStatus.fromValue(rs.getString(columnIndex)); // 한글 값 → Enum 변환
    }

    @Override
    public AttendanceStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AttendanceStatus.fromValue(cs.getString(columnIndex)); // 한글 값 → Enum 변환
    }
}