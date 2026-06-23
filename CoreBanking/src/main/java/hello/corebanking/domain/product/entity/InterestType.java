package hello.corebanking.domain.product.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class InterestType {

    private int interestTypeId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InterestType(String name) {
        this.name = name;
    }
}
