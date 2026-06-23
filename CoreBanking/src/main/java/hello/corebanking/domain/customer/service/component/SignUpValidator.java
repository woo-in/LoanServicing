package hello.corebanking.domain.customer.service.component;

import hello.corebanking.domain.customer.dto.SignUpRequest;
import hello.corebanking.domain.customer.repository.MemberMapper;
import hello.corebanking.global.exception.DuplicateLoginIdException;
import hello.corebanking.global.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignUpValidator {

    private final MemberMapper memberMapper;

    public void validate(SignUpRequest request) {
        if (request.getPassword() == null || !request.getPassword().equals(request.getPasswordConfirm())) {
            throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
        }

        if (memberMapper.existsByLoginId(request.getLoginId())) {
            throw new DuplicateLoginIdException("이미 존재하는 아이디입니다.");
        }
    }
}
