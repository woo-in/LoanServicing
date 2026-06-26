package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.repayment.entity.DelinquencyReason;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DelinquencyReasonTypeHandler extends BaseTypeHandler<DelinquencyReason> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DelinquencyReason parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public DelinquencyReason getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : DelinquencyReason.fromId(id);
    }

    @Override
    public DelinquencyReason getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : DelinquencyReason.fromId(id);
    }

    @Override
    public DelinquencyReason getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : DelinquencyReason.fromId(id);
    }
}
