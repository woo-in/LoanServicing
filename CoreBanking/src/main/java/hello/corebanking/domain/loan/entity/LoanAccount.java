package hello.corebanking.domain.loan.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LoanAccount {

    private long loanAccountId;
    private long loanContractId;
    private long primaryAccountId;
    private String accountNo;
    private BigDecimal loanPrincipalBalance;
    private int monthlyPaymentDay;
    private int currentInstallmentNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
