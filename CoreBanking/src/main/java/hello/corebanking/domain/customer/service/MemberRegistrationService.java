package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.MemberRegistrationResult;
import hello.corebanking.domain.customer.dto.OpenPrimaryAccountRequest;
import hello.corebanking.domain.customer.dto.SignUpRequest;
import hello.corebanking.domain.customer.entity.Member;
import hello.corebanking.domain.customer.entity.PrimaryAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRegistrationService {

    private final MemberService memberService;
    private final PrimaryAccountService primaryAccountService;

    @Transactional
    public MemberRegistrationResult signUp(SignUpRequest request) {
        Member member = memberService.signUp(request);
        PrimaryAccount primaryAccount = primaryAccountService.open(
                new OpenPrimaryAccountRequest(member.getMemberId())
        );
        return new MemberRegistrationResult(member, primaryAccount);
    }
}
