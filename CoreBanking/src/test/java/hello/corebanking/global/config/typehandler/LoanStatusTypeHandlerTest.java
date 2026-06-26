package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.loan.entity.LoanStatus;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoanStatusTypeHandlerTest {

    private LoanStatusTypeHandler handler;
    private PreparedStatement ps;
    private ResultSet rs;

    @BeforeEach
    void setUp() {
        handler = new LoanStatusTypeHandler();
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }

    @Test
    @DisplayName("setNonNullParameter 호출 시 Enum의 id를 int로 설정한다")
    void setNonNullParameter() throws Exception {
        handler.setNonNullParameter(ps, 1, LoanStatus.NORMAL, JdbcType.INTEGER);
        verify(ps).setInt(1, 1);

        handler.setNonNullParameter(ps, 1, LoanStatus.ACCELERATED, JdbcType.INTEGER);
        verify(ps).setInt(1, 4);
    }

    @Test
    @DisplayName("컬럼명으로 조회 시 int id를 올바른 Enum으로 변환한다")
    void getNullableResult_byColumnName() throws Exception {
        when(rs.getInt("loan_status_id")).thenReturn(2);
        when(rs.wasNull()).thenReturn(false);

        assertThat(handler.getNullableResult(rs, "loan_status_id")).isEqualTo(LoanStatus.DELINQUENT);
    }

    @Test
    @DisplayName("컬럼 인덱스로 조회 시 int id를 올바른 Enum으로 변환한다")
    void getNullableResult_byColumnIndex() throws Exception {
        when(rs.getInt(1)).thenReturn(3);
        when(rs.wasNull()).thenReturn(false);

        assertThat(handler.getNullableResult(rs, 1)).isEqualTo(LoanStatus.PAID_OFF);
    }

    @Test
    @DisplayName("DB 값이 NULL이면 null을 반환한다")
    void getNullableResult_whenNull() throws Exception {
        when(rs.getInt("loan_status_id")).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);

        assertThat(handler.getNullableResult(rs, "loan_status_id")).isNull();
    }
}
