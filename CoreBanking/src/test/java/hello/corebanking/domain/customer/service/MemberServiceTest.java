package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.SignUpRequest;
import hello.corebanking.domain.customer.entity.Member;
import hello.corebanking.domain.customer.repository.MemberMapper;
import hello.corebanking.domain.customer.service.component.SignUpValidator;
import hello.corebanking.global.exception.DuplicateLoginIdException;
import hello.corebanking.global.exception.NotFoundException;
import hello.corebanking.global.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SignUpValidator signUpValidator;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("정상 회원가입 시 비밀번호가 암호화되고 회원이 저장된다")
    void signUp_success() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "Pass!1234", "홍길동");
        when(passwordEncoder.encode("Pass!1234")).thenReturn("encoded_password");

        Member result = memberService.signUp(request);

        assertThat(result.getLoginId()).isEqualTo("user01");
        assertThat(result.getPassword()).isEqualTo("encoded_password");
        assertThat(result.getName()).isEqualTo("홍길동");
        verify(signUpValidator).validate(request);
        verify(memberMapper).insert(any(Member.class));
    }

    @Test
    @DisplayName("비밀번호 불일치 시 PasswordMismatchException 을 던진다")
    void signUp_passwordMismatch() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "wrongPass", "홍길동");
        org.mockito.Mockito.doThrow(new PasswordMismatchException("비밀번호가 일치하지 않습니다."))
                .when(signUpValidator).validate(request);

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(PasswordMismatchException.class);
        verify(memberMapper, never()).insert(any());
    }

    @Test
    @DisplayName("중복 아이디로 가입 시 DuplicateLoginIdException 을 던진다")
    void signUp_duplicateLoginId() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "Pass!1234", "홍길동");
        org.mockito.Mockito.doThrow(new DuplicateLoginIdException("이미 존재하는 아이디입니다."))
                .when(signUpValidator).validate(request);

        assertThatThrownBy(() -> memberService.signUp(request))
                .isInstanceOf(DuplicateLoginIdException.class);
        verify(memberMapper, never()).insert(any());
    }

    @Test
    @DisplayName("존재하는 loginId 로 조회 시 회원을 반환한다")
    void findByLoginId_found() {
        when(memberMapper.findByLoginId("user01")).thenReturn(Optional.of(new Member("user01", "encoded", "홍길동")));

        Member result = memberService.findByLoginId("user01");

        assertThat(result.getLoginId()).isEqualTo("user01");
    }

    @Test
    @DisplayName("존재하지 않는 loginId 로 조회 시 NotFoundException 을 던진다")
    void findByLoginId_notFound() {
        when(memberMapper.findByLoginId(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findByLoginId("unknown"))
                .isInstanceOf(NotFoundException.class);
    }
}
