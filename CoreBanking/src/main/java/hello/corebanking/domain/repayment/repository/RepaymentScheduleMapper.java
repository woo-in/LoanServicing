package hello.corebanking.domain.repayment.repository;

import hello.corebanking.domain.repayment.entity.RepaymentScheduleStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface RepaymentScheduleMapper {

    BigDecimal sumScheduledPrincipalByLoanAccountId(
            @Param("loanAccountId") long loanAccountId,
            @Param("excludedStatus") RepaymentScheduleStatus excludedStatus);
}
