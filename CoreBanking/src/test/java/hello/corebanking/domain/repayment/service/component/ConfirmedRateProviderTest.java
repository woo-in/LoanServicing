package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.dto.ContractedRate;
import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.product.dto.LoanTerms;
import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmedRateProviderTest {

    @Mock
    private ContractedRateProvider contractedRateProvider;

    @Mock
    private LoanTermsProvider loanTermsProvider;

    @Mock
    private BaseRateProvider baseRateProvider;

    @InjectMocks
    private ConfirmedRateProvider confirmedRateProvider;

    private static final ContractedRate CONTRACTED_RATE =
            new ContractedRate(new BigDecimal("3.00"), new BigDecimal("1.20"));

    @Test
    @DisplayName("LoanContract, 고정금리인 경우 계약금리를 그대로 확정 금리로 반환한다")
    void getConfirmedRate_LoanContract_고정금리() {
        LoanContract contract = mock(LoanContract.class);
        when(contractedRateProvider.getContractedRate(contract)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(contract))
                .thenReturn(new LoanTerms(InterestType.FIXED, RepaymentMethod.LEVEL_PAYMENT));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(contract);

        assertThat(result).isEqualByComparingTo("4.20");
        verifyNoInteractions(baseRateProvider);
    }

    @Test
    @DisplayName("LoanContract, 변동금리인 경우 기준금리 + 가산금리를 확정 금리로 반환한다")
    void getConfirmedRate_LoanContract_변동금리() {
        LoanContract contract = mock(LoanContract.class);
        when(contractedRateProvider.getContractedRate(contract)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(contract))
                .thenReturn(new LoanTerms(InterestType.VARIABLE, RepaymentMethod.LEVEL_PAYMENT));
        when(baseRateProvider.getBaseRate()).thenReturn(new BigDecimal("3.50"));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(contract);

        assertThat(result).isEqualByComparingTo("4.70");
    }

    @Test
    @DisplayName("LoanAccount, 고정금리인 경우 계약금리를 그대로 확정 금리로 반환한다")
    void getConfirmedRate_LoanAccount_고정금리() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(contractedRateProvider.getContractedRate(loanAccount)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(loanAccount))
                .thenReturn(new LoanTerms(InterestType.FIXED, RepaymentMethod.EQUAL_PRINCIPAL));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(loanAccount);

        assertThat(result).isEqualByComparingTo("4.20");
        verifyNoInteractions(baseRateProvider);
    }

    @Test
    @DisplayName("LoanAccount, 변동금리인 경우 기준금리 + 가산금리를 확정 금리로 반환한다")
    void getConfirmedRate_LoanAccount_변동금리() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(contractedRateProvider.getContractedRate(loanAccount)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(loanAccount))
                .thenReturn(new LoanTerms(InterestType.VARIABLE, RepaymentMethod.EQUAL_PRINCIPAL));
        when(baseRateProvider.getBaseRate()).thenReturn(new BigDecimal("3.50"));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(loanAccount);

        assertThat(result).isEqualByComparingTo("4.70");
    }

    @Test
    @DisplayName("RepaymentSchedule, 고정금리인 경우 계약금리를 그대로 확정 금리로 반환한다")
    void getConfirmedRate_RepaymentSchedule_고정금리() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(contractedRateProvider.getContractedRate(schedule)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(schedule))
                .thenReturn(new LoanTerms(InterestType.FIXED, RepaymentMethod.BULLET));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(schedule);

        assertThat(result).isEqualByComparingTo("4.20");
        verifyNoInteractions(baseRateProvider);
    }

    @Test
    @DisplayName("RepaymentSchedule, 변동금리인 경우 기준금리 + 가산금리를 확정 금리로 반환한다")
    void getConfirmedRate_RepaymentSchedule_변동금리() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(contractedRateProvider.getContractedRate(schedule)).thenReturn(CONTRACTED_RATE);
        when(loanTermsProvider.getLoanTerms(schedule))
                .thenReturn(new LoanTerms(InterestType.VARIABLE, RepaymentMethod.BULLET));
        when(baseRateProvider.getBaseRate()).thenReturn(new BigDecimal("3.50"));

        BigDecimal result = confirmedRateProvider.getConfirmedRate(schedule);

        assertThat(result).isEqualByComparingTo("4.70");
    }
}
