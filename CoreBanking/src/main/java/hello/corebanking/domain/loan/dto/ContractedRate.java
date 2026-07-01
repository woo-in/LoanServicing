package hello.corebanking.domain.loan.dto;

import java.math.BigDecimal;

public record ContractedRate(BigDecimal contractBaseRate, BigDecimal contractAdditionalRate) {
}
