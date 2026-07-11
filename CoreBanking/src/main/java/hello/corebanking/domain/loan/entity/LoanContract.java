package hello.corebanking.domain.loan.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LoanContract {

    private long loanContractId;
    private long loanProductId;
    private long memberId;
    private LocalDate contractDate;
    private LocalDate maturityDate;
    private BigDecimal contractPrincipal;
    private BigDecimal contractAdditionalRate;
    private BigDecimal contractBaseRate;
    private int totalInstallments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
