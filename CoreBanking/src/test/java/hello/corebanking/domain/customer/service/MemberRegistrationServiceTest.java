package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.MemberRegistrationResult;
import hello.corebanking.domain.customer.dto.OpenPrimaryAccountRequest;
import hello.corebanking.domain.customer.dto.SignUpRequest;
import hello.corebanking.domain.customer.entity.Member;
import hello.corebanking.domain.customer.entity.PrimaryAccount;
import hello.corebanking.global.exception.DuplicateLoginIdException;
import hello.corebanking.global.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberRegistrationServiceTest {

    @Mock
    private MemberService memberService;
    @Mock
    private PrimaryAccountService primaryAccountService;

    @InjectMocks
    private MemberRegistrationService memberRegistrationService;

    @Test
    @DisplayName("정상 가입 시 회원과 주계좌가 함께 생성되고 결과를 반환한다")
    void signUp_success() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "Pass!1234", "홍길동");
        Member member = new Member("user01", "encoded", "홍길동");
        PrimaryAccount primaryAccount = new PrimaryAccount(0L, "110-260624-123456");

        when(memberService.signUp(request)).thenReturn(member);
        when(primaryAccountService.open(any(OpenPrimaryAccountRequest.class))).thenReturn(primaryAccount);

        MemberRegistrationResult result = memberRegistrationService.signUp(request);

        assertThat(result.getMember().getLoginId()).isEqualTo("user01");
        assertThat(result.getPrimaryAccount().getAccountNo()).isEqualTo("110-260624-123456");
        verify(memberService).signUp(request);
        verify(primaryAccountService).open(any(OpenPrimaryAccountRequest.class));
    }

    @Test
    @DisplayName("회원가입 실패 시 주계좌 개설이 호출되지 않는다")
    void signUp_memberFails() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "wrongPass", "홍길동");
        when(memberService.signUp(request)).thenThrow(new PasswordMismatchException("비밀번호가 일치하지 않습니다."));

        assertThatThrownBy(() -> memberRegistrationService.signUp(request))
                .isInstanceOf(PasswordMismatchException.class);
        verify(primaryAccountService, never()).open(any());
    }

    @Test
    @DisplayName("중복 아이디로 가입 시 주계좌 개설이 호출되지 않는다")
    void signUp_duplicateLoginId() {
        SignUpRequest request = new SignUpRequest("user01", "Pass!1234", "Pass!1234", "홍길동");
        when(memberService.signUp(request)).thenThrow(new DuplicateLoginIdException("이미 존재하는 아이디입니다."));

        assertThatThrownBy(() -> memberRegistrationService.signUp(request))
                .isInstanceOf(DuplicateLoginIdException.class);
        verify(primaryAccountService, never()).open(any());
    }
}
