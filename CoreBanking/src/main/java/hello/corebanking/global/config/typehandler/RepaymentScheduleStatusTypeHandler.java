package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.repayment.entity.RepaymentScheduleStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepaymentScheduleStatusTypeHandler extends BaseTypeHandler<RepaymentScheduleStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, RepaymentScheduleStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public RepaymentScheduleStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : RepaymentScheduleStatus.fromId(id);
    }

    @Override
    public RepaymentScheduleStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : RepaymentScheduleStatus.fromId(id);
    }

    @Override
    public RepaymentScheduleStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : RepaymentScheduleStatus.fromId(id);
    }
}
