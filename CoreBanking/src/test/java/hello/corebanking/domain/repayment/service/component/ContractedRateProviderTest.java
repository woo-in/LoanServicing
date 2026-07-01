package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.dto.ContractedRate;
import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
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
class ContractedRateProviderTest {

    @Mock
    private LoanContractMapper loanContractMapper;

    @InjectMocks
    private ContractedRateProvider contractedRateProvider;

    @Test
    @DisplayName("LoanContract 엔티티로 금리를 직접 반환한다 - Mapper 호출 없음")
    void getContractedRate_LoanContract_금리_직접_반환() {
        LoanContract contract = mock(LoanContract.class);
        when(contract.getContractBaseRate()).thenReturn(new BigDecimal("3.50"));
        when(contract.getContractAdditionalRate()).thenReturn(new BigDecimal("1.20"));

        ContractedRate result = contractedRateProvider.getContractedRate(contract);

        assertThat(result.contractBaseRate()).isEqualByComparingTo("3.50");
        assertThat(result.contractAdditionalRate()).isEqualByComparingTo("1.20");
        verifyNoInteractions(loanContractMapper);
    }

    @Test
    @DisplayName("LoanAccount 엔티티로 계약 금리를 반환한다")
    void getContractedRate_LoanAccount_금리_반환() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getLoanContractId()).thenReturn(1L);

        LoanContract contract = mock(LoanContract.class);
        when(contract.getContractBaseRate()).thenReturn(new BigDecimal("3.50"));
        when(contract.getContractAdditionalRate()).thenReturn(new BigDecimal("1.20"));
        when(loanContractMapper.findById(1L)).thenReturn(Optional.of(contract));

        ContractedRate result = contractedRateProvider.getContractedRate(loanAccount);

        assertThat(result.contractBaseRate()).isEqualByComparingTo("3.50");
        assertThat(result.contractAdditionalRate()).isEqualByComparingTo("1.20");
    }

    @Test
    @DisplayName("LoanAccount에 해당하는 계약이 없으면 NotFoundException을 던진다")
    void getContractedRate_LoanAccount_계약없음_NotFoundException() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getLoanContractId()).thenReturn(99L);
        when(loanContractMapper.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractedRateProvider.getContractedRate(loanAccount))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("RepaymentSchedule 엔티티로 계약 금리를 반환한다")
    void getContractedRate_RepaymentSchedule_금리_반환() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(schedule.getLoanAccountId()).thenReturn(2L);

        LoanContract contract = mock(LoanContract.class);
        when(contract.getContractBaseRate()).thenReturn(new BigDecimal("3.50"));
        when(contract.getContractAdditionalRate()).thenReturn(new BigDecimal("1.20"));
        when(loanContractMapper.findByLoanAccountId(2L)).thenReturn(Optional.of(contract));

        ContractedRate result = contractedRateProvider.getContractedRate(schedule);

        assertThat(result.contractBaseRate()).isEqualByComparingTo("3.50");
        assertThat(result.contractAdditionalRate()).isEqualByComparingTo("1.20");
    }

    @Test
    @DisplayName("RepaymentSchedule에 해당하는 계약이 없으면 NotFoundException을 던진다")
    void getContractedRate_RepaymentSchedule_계약없음_NotFoundException() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(schedule.getLoanAccountId()).thenReturn(99L);
        when(loanContractMapper.findByLoanAccountId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractedRateProvider.getContractedRate(schedule))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }
}
