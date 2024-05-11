package com.moz1mozi.mybatis.common.typehandler;

import com.moz1mozi.mybatis.member.dto.Role;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Role.class)
public class RoleTypeHandler implements TypeHandler<Role> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Role parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getDisplayName());
    }

    @Override
    public Role getResult(ResultSet rs, String columnName) throws SQLException {
        String displayName = rs.getString(columnName);
        return displayNameToRole(displayName);
    }

    @Override
    public Role getResult(ResultSet rs, int columnIndex) throws SQLException {
        String displayName = rs.getString(columnIndex);
        return displayNameToRole(displayName);
    }

    @Override
    public Role getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String displayName = cs.getString(columnIndex);
        return displayNameToRole(displayName);
    }

    private Role displayNameToRole(String displayName) {
        for (Role role : Role.values()) {
            if (role.getDisplayName().equals(displayName)) {
                return role;
            }
        }
        return Role.BUYER;
//        throw new IllegalArgumentException("Unknown displayName: " + displayName);
    }
}
