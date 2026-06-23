package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.SignUpRequest;
import hello.corebanking.domain.customer.entity.Member;
import hello.corebanking.domain.customer.repository.MemberMapper;
import hello.corebanking.domain.customer.service.component.SignUpValidator;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidator signUpValidator;

    @Transactional
    public Member signUp(SignUpRequest request) {
        signUpValidator.validate(request);

        Member member = new Member(
                request.getLoginId(),
                passwordEncoder.encode(request.getPassword()),
                request.getName()
        );
        memberMapper.insert(member);
        return member;
    }

    @Transactional(readOnly = true)
    public Member findByLoginId(String loginId) {
        return memberMapper.findByLoginId(loginId)
                .orElseThrow(() -> new NotFoundException("회원을 찾을 수 없습니다. loginId=" + loginId));
    }
}
