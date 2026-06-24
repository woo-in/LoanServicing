package hello.corebanking.domain.customer.dto;

import hello.corebanking.domain.customer.entity.Member;
import hello.corebanking.domain.customer.entity.PrimaryAccount;
import lombok.Getter;

@Getter
public class MemberRegistrationResult {

    private final Member member;
    private final PrimaryAccount primaryAccount;

    public MemberRegistrationResult(Member member, PrimaryAccount primaryAccount) {
        this.member = member;
        this.primaryAccount = primaryAccount;
    }
}
