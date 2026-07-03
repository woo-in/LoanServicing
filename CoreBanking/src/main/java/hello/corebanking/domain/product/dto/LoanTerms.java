package hello.corebanking.domain.product.dto;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.RepaymentMethod;

public record LoanTerms(InterestType interestType, RepaymentMethod repaymentMethod) {
}
