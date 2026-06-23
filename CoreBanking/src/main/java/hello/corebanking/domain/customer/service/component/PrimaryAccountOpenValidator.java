package hello.corebanking.domain.customer.service.component;

import hello.corebanking.domain.customer.dto.OpenPrimaryAccountRequest;
import hello.corebanking.domain.customer.repository.MemberMapper;
import hello.corebanking.domain.customer.repository.PrimaryAccountMapper;
import hello.corebanking.global.exception.DuplicateAccountException;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrimaryAccountOpenValidator {

    private final MemberMapper memberMapper;
    private final PrimaryAccountMapper primaryAccountMapper;

    public void validate(OpenPrimaryAccountRequest request) {
        if (!memberMapper.existsById(request.getMemberId())) {
            throw new NotFoundException("회원을 찾을 수 없습니다. memberId=" + request.getMemberId());
        }

        if (primaryAccountMapper.existsByMemberId(request.getMemberId())) {
            throw new DuplicateAccountException("이미 주계좌가 존재합니다. memberId=" + request.getMemberId());
        }
    }
}
