package hello.corebanking.domain.repayment.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RepaymentSchedule {

    private long repaymentScheduleId;
    private long loanAccountId;
    private RepaymentScheduleStatus repaymentScheduleStatus;
    private LocalDateTime scheduledAt;
    private BigDecimal scheduledPrincipal;
    private BigDecimal scheduledInterest;
    private BigDecimal appliedAdditionalRate;
    private BigDecimal appliedBaseRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
