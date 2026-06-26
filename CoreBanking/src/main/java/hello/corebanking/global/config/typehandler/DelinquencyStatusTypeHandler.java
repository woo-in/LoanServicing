package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.repayment.entity.DelinquencyStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DelinquencyStatusTypeHandler extends BaseTypeHandler<DelinquencyStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, DelinquencyStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public DelinquencyStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : DelinquencyStatus.fromId(id);
    }

    @Override
    public DelinquencyStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : DelinquencyStatus.fromId(id);
    }

    @Override
    public DelinquencyStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : DelinquencyStatus.fromId(id);
    }
}
