package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.repayment.entity.RepaymentScheduleStatus;
import hello.corebanking.domain.repayment.repository.RepaymentScheduleMapper;
import hello.corebanking.global.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpectedBalanceProviderTest {

    @Mock
    private LoanContractMapper loanContractMapper;

    @Mock
    private RepaymentScheduleMapper repaymentScheduleMapper;

    @InjectMocks
    private ExpectedBalanceProvider expectedBalanceProvider;

    @Test
    @DisplayName("도래한 회차가 없으면 계약원금을 그대로 반환한다")
    void getExpectedBalance_도래한_회차가_없으면_계약원금을_그대로_반환한다() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        LoanContract contract = mock(LoanContract.class);
        when(loanAccount.getLoanContractId()).thenReturn(1L);
        when(loanAccount.getLoanAccountId()).thenReturn(10L);
        when(loanContractMapper.findById(1L)).thenReturn(Optional.of(contract));
        when(contract.getContractPrincipal()).thenReturn(new BigDecimal("10000000"));
        when(repaymentScheduleMapper.sumScheduledPrincipalByLoanAccountId(10L, RepaymentScheduleStatus.SCHEDULED))
                .thenReturn(BigDecimal.ZERO);

        BigDecimal result = expectedBalanceProvider.getExpectedBalance(loanAccount);

        assertThat(result).isEqualByComparingTo("10000000");
    }

    @Test
    @DisplayName("도래한 회차들의 원금 합계를 계약원금에서 차감한다")
    void getExpectedBalance_도래한_회차들의_원금_합계를_계약원금에서_차감한다() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        LoanContract contract = mock(LoanContract.class);
        when(loanAccount.getLoanContractId()).thenReturn(1L);
        when(loanAccount.getLoanAccountId()).thenReturn(10L);
        when(loanContractMapper.findById(1L)).thenReturn(Optional.of(contract));
        when(contract.getContractPrincipal()).thenReturn(new BigDecimal("10000000"));
        when(repaymentScheduleMapper.sumScheduledPrincipalByLoanAccountId(10L, RepaymentScheduleStatus.SCHEDULED))
                .thenReturn(new BigDecimal("2500000"));

        BigDecimal result = expectedBalanceProvider.getExpectedBalance(loanAccount);

        assertThat(result).isEqualByComparingTo("7500000");
    }

    @Test
    @DisplayName("SCHEDULED 상태를 제외하고 합계를 조회한다")
    void getExpectedBalance_SCHEDULED_상태를_제외하고_합계를_조회한다() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        LoanContract contract = mock(LoanContract.class);
        when(loanAccount.getLoanContractId()).thenReturn(1L);
        when(loanAccount.getLoanAccountId()).thenReturn(10L);
        when(loanContractMapper.findById(1L)).thenReturn(Optional.of(contract));
        when(contract.getContractPrincipal()).thenReturn(new BigDecimal("10000000"));
        when(repaymentScheduleMapper.sumScheduledPrincipalByLoanAccountId(10L, RepaymentScheduleStatus.SCHEDULED))
                .thenReturn(BigDecimal.ZERO);

        expectedBalanceProvider.getExpectedBalance(loanAccount);

        verify(repaymentScheduleMapper)
                .sumScheduledPrincipalByLoanAccountId(eq(10L), eq(RepaymentScheduleStatus.SCHEDULED));
    }

    @Test
    @DisplayName("계약을 찾을 수 없으면 NotFoundException을 던진다")
    void getExpectedBalance_계약을_찾을_수_없으면_NotFoundException을_던진다() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getLoanContractId()).thenReturn(99L);
        when(loanContractMapper.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> expectedBalanceProvider.getExpectedBalance(loanAccount))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
        verifyNoInteractions(repaymentScheduleMapper);
    }
}
