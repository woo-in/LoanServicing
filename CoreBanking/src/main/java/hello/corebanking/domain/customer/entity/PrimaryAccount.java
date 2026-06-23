package hello.corebanking.domain.customer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PrimaryAccount {

    private long primaryAccountId;
    private long memberId;
    private String accountNo;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PrimaryAccount(long memberId, String accountNo) {
        this.memberId = memberId;
        this.accountNo = accountNo;
    }
}
