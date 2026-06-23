package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.OpenPrimaryAccountRequest;
import hello.corebanking.domain.customer.entity.PrimaryAccount;
import hello.corebanking.domain.customer.repository.PrimaryAccountMapper;
import hello.corebanking.domain.customer.service.component.PrimaryAccountOpenValidator;
import hello.corebanking.global.exception.DuplicateAccountException;
import hello.corebanking.global.exception.NotFoundException;
import hello.corebanking.global.util.AccountNumberGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrimaryAccountServiceTest {

    @Mock
    private PrimaryAccountMapper primaryAccountMapper;
    @Mock
    private PrimaryAccountOpenValidator validator;
    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private PrimaryAccountService primaryAccountService;

    @Test
    @DisplayName("정상 주계좌 개설 시 계좌번호가 생성되고 저장된다")
    void open_success() {
        OpenPrimaryAccountRequest request = new OpenPrimaryAccountRequest(1L);
        when(accountNumberGenerator.generate()).thenReturn("110-260623-123456");

        PrimaryAccount result = primaryAccountService.open(request);

        assertThat(result.getMemberId()).isEqualTo(1L);
        assertThat(result.getAccountNo()).isEqualTo("110-260623-123456");
        verify(validator).validate(request);
        verify(primaryAccountMapper).insert(any(PrimaryAccount.class));
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 개설 시 NotFoundException 을 던진다")
    void open_memberNotFound() {
        OpenPrimaryAccountRequest request = new OpenPrimaryAccountRequest(99L);
        doThrow(new NotFoundException("회원을 찾을 수 없습니다. memberId=99"))
                .when(validator).validate(request);

        assertThatThrownBy(() -> primaryAccountService.open(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
        verify(primaryAccountMapper, never()).insert(any());
    }

    @Test
    @DisplayName("주계좌가 이미 존재하는 회원으로 개설 시 DuplicateAccountException 을 던진다")
    void open_duplicateAccount() {
        OpenPrimaryAccountRequest request = new OpenPrimaryAccountRequest(1L);
        doThrow(new DuplicateAccountException("이미 주계좌가 존재합니다. memberId=1"))
                .when(validator).validate(request);

        assertThatThrownBy(() -> primaryAccountService.open(request))
                .isInstanceOf(DuplicateAccountException.class);
        verify(primaryAccountMapper, never()).insert(any());
    }

    @Test
    @DisplayName("존재하는 memberId 로 조회 시 주계좌를 반환한다")
    void findByMemberId_found() {
        PrimaryAccount account = new PrimaryAccount(1L, "110-260623-123456");
        when(primaryAccountMapper.findByMemberId(1L)).thenReturn(Optional.of(account));

        PrimaryAccount result = primaryAccountService.findByMemberId(1L);

        assertThat(result.getAccountNo()).isEqualTo("110-260623-123456");
    }

    @Test
    @DisplayName("존재하지 않는 memberId 로 조회 시 NotFoundException 을 던진다")
    void findByMemberId_notFound() {
        when(primaryAccountMapper.findByMemberId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> primaryAccountService.findByMemberId(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}
