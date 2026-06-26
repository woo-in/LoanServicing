package hello.corebanking.global.config.typehandler;

import hello.corebanking.domain.repayment.entity.DelinquencyReason;
import org.apache.ibatis.type.JdbcType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DelinquencyReasonTypeHandlerTest {

    private DelinquencyReasonTypeHandler handler;
    private PreparedStatement ps;
    private ResultSet rs;

    @BeforeEach
    void setUp() {
        handler = new DelinquencyReasonTypeHandler();
        ps = mock(PreparedStatement.class);
        rs = mock(ResultSet.class);
    }

    @Test
    @DisplayName("setNonNullParameter 호출 시 Enum의 id를 int로 설정한다")
    void setNonNullParameter() throws Exception {
        handler.setNonNullParameter(ps, 1, DelinquencyReason.BULLET_INTEREST_SHORT, JdbcType.INTEGER);
        verify(ps).setInt(1, 1);

        handler.setNonNullParameter(ps, 1, DelinquencyReason.INSTALLMENT_CONSECUTIVE_PROTECTED, JdbcType.INTEGER);
        verify(ps).setInt(1, 7);
    }

    @Test
    @DisplayName("컬럼명으로 조회 시 int id를 올바른 Enum으로 변환한다")
    void getNullableResult_byColumnName() throws Exception {
        when(rs.getInt("delinquency_reason_id")).thenReturn(5);
        when(rs.wasNull()).thenReturn(false);

        assertThat(handler.getNullableResult(rs, "delinquency_reason_id")).isEqualTo(DelinquencyReason.INSTALLMENT_SINGLE);
    }

    @Test
    @DisplayName("컬럼 인덱스로 조회 시 int id를 올바른 Enum으로 변환한다")
    void getNullableResult_byColumnIndex() throws Exception {
        when(rs.getInt(1)).thenReturn(4);
        when(rs.wasNull()).thenReturn(false);

        assertThat(handler.getNullableResult(rs, 1)).isEqualTo(DelinquencyReason.BULLET_PRINCIPAL);
    }

    @Test
    @DisplayName("DB 값이 NULL이면 null을 반환한다")
    void getNullableResult_whenNull() throws Exception {
        when(rs.getInt("delinquency_reason_id")).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);

        assertThat(handler.getNullableResult(rs, "delinquency_reason_id")).isNull();
    }
}
