package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.loan.entity.LoanStatus;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoanStatusTypeHandler extends BaseTypeHandler<LoanStatus> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LoanStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getId());
    }

    @Override
    public LoanStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int id = rs.getInt(columnName);
        return rs.wasNull() ? null : LoanStatus.fromId(id);
    }

    @Override
    public LoanStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int id = rs.getInt(columnIndex);
        return rs.wasNull() ? null : LoanStatus.fromId(id);
    }

    @Override
    public LoanStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int id = cs.getInt(columnIndex);
        return cs.wasNull() ? null : LoanStatus.fromId(id);
    }
}
