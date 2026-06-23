package hello.corebanking.domain.product.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RepaymentMethod {

    private int repaymentMethodId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RepaymentMethod(String name) {
        this.name = name;
    }
}
