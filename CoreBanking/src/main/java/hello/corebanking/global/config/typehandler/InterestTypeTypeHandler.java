package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.product.entity.InterestType;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InterestTypeTypeHandler extends BaseTypeHandler<InterestType> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, InterestType parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public InterestType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : InterestType.fromId(id);
    }

    @Override
    public InterestType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : InterestType.fromId(id);
    }

    @Override
    public InterestType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : InterestType.fromId(id);
    }
}
