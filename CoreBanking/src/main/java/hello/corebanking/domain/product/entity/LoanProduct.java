package hello.corebanking.domain.product.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LoanProduct {

    private long loanProductId;
    private InterestType interestType;
    private RepaymentMethod repaymentMethod;
    private String name;
    private BigDecimal additionalRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LoanProduct(InterestType interestType, RepaymentMethod repaymentMethod,
                       String name, BigDecimal additionalRate) {
        this.interestType = interestType;
        this.repaymentMethod = repaymentMethod;
        this.name = name;
        this.additionalRate = additionalRate;
    }
}
