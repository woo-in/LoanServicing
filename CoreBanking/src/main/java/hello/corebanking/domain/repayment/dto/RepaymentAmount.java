package hello.corebanking.domain.repayment.dto;

import java.math.BigDecimal;

public record RepaymentAmount(BigDecimal principal, BigDecimal interest) {
}
