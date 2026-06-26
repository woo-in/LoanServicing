package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepaymentMethodTypeHandler extends BaseTypeHandler<RepaymentMethod> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, RepaymentMethod parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public RepaymentMethod getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : RepaymentMethod.fromId(id);
    }

    @Override
    public RepaymentMethod getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : RepaymentMethod.fromId(id);
    }

    @Override
    public RepaymentMethod getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : RepaymentMethod.fromId(id);
    }
}
