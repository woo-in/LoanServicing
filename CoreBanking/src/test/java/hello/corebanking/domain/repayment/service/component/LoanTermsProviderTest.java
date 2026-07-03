package hello.corebanking.domain.repayment.service.component;

import hello.corebanking.domain.loan.entity.LoanAccount;
import hello.corebanking.domain.loan.entity.LoanContract;
import hello.corebanking.domain.loan.repository.LoanContractMapper;
import hello.corebanking.domain.product.dto.LoanTerms;
import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.LoanProduct;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.LoanProductMapper;
import hello.corebanking.domain.repayment.entity.RepaymentSchedule;
import hello.corebanking.global.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanTermsProviderTest {

    @Mock
    private LoanContractMapper loanContractMapper;

    @Mock
    private LoanProductMapper loanProductMapper;

    @InjectMocks
    private LoanTermsProvider loanTermsProvider;

    @Test
    @DisplayName("LoanContract 엔티티로 대출조건을 반환한다 - LoanContractMapper 호출 없음")
    void getLoanTerms_LoanContract_대출조건_반환() {
        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(1L);

        LoanProduct product = mock(LoanProduct.class);
        when(product.getInterestType()).thenReturn(InterestType.FIXED);
        when(product.getRepaymentMethod()).thenReturn(RepaymentMethod.LEVEL_PAYMENT);
        when(loanProductMapper.findById(1L)).thenReturn(Optional.of(product));

        LoanTerms result = loanTermsProvider.getLoanTerms(contract);

        assertThat(result.interestType()).isEqualTo(InterestType.FIXED);
        assertThat(result.repaymentMethod()).isEqualTo(RepaymentMethod.LEVEL_PAYMENT);
        verifyNoInteractions(loanContractMapper);
    }

    @Test
    @DisplayName("LoanAccount 엔티티로 대출조건을 반환한다")
    void getLoanTerms_LoanAccount_대출조건_반환() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getLoanContractId()).thenReturn(1L);

        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(10L);
        when(loanContractMapper.findById(1L)).thenReturn(Optional.of(contract));

        LoanProduct product = mock(LoanProduct.class);
        when(product.getInterestType()).thenReturn(InterestType.VARIABLE);
        when(product.getRepaymentMethod()).thenReturn(RepaymentMethod.EQUAL_PRINCIPAL);
        when(loanProductMapper.findById(10L)).thenReturn(Optional.of(product));

        LoanTerms result = loanTermsProvider.getLoanTerms(loanAccount);

        assertThat(result.interestType()).isEqualTo(InterestType.VARIABLE);
        assertThat(result.repaymentMethod()).isEqualTo(RepaymentMethod.EQUAL_PRINCIPAL);
    }

    @Test
    @DisplayName("LoanAccount에 해당하는 계약이 없으면 NotFoundException을 던진다")
    void getLoanTerms_LoanAccount_계약없음_NotFoundException() {
        LoanAccount loanAccount = mock(LoanAccount.class);
        when(loanAccount.getLoanContractId()).thenReturn(99L);
        when(loanContractMapper.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanTermsProvider.getLoanTerms(loanAccount))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("RepaymentSchedule 엔티티로 대출조건을 반환한다")
    void getLoanTerms_RepaymentSchedule_대출조건_반환() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(schedule.getLoanAccountId()).thenReturn(2L);

        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(20L);
        when(loanContractMapper.findByLoanAccountId(2L)).thenReturn(Optional.of(contract));

        LoanProduct product = mock(LoanProduct.class);
        when(product.getInterestType()).thenReturn(InterestType.FIXED);
        when(product.getRepaymentMethod()).thenReturn(RepaymentMethod.BULLET);
        when(loanProductMapper.findById(20L)).thenReturn(Optional.of(product));

        LoanTerms result = loanTermsProvider.getLoanTerms(schedule);

        assertThat(result.interestType()).isEqualTo(InterestType.FIXED);
        assertThat(result.repaymentMethod()).isEqualTo(RepaymentMethod.BULLET);
    }

    @Test
    @DisplayName("RepaymentSchedule에 해당하는 계약이 없으면 NotFoundException을 던진다")
    void getLoanTerms_RepaymentSchedule_계약없음_NotFoundException() {
        RepaymentSchedule schedule = mock(RepaymentSchedule.class);
        when(schedule.getLoanAccountId()).thenReturn(99L);
        when(loanContractMapper.findByLoanAccountId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanTermsProvider.getLoanTerms(schedule))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("계약은 있으나 대출상품이 없으면 NotFoundException을 던진다")
    void getLoanTerms_대출상품없음_NotFoundException() {
        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(999L);
        when(loanProductMapper.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanTermsProvider.getLoanTerms(contract))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    @DisplayName("대출상품의 금리종류가 없으면 NotFoundException을 던진다")
    void getLoanTerms_금리종류없음_NotFoundException() {
        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(1L);

        LoanProduct product = mock(LoanProduct.class);
        when(product.getInterestType()).thenReturn(null);
        when(product.getLoanProductId()).thenReturn(1L);
        when(loanProductMapper.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> loanTermsProvider.getLoanTerms(contract))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("1");
    }

    @Test
    @DisplayName("대출상품의 상환방법이 없으면 NotFoundException을 던진다")
    void getLoanTerms_상환방법없음_NotFoundException() {
        LoanContract contract = mock(LoanContract.class);
        when(contract.getLoanProductId()).thenReturn(1L);

        LoanProduct product = mock(LoanProduct.class);
        when(product.getInterestType()).thenReturn(InterestType.FIXED);
        when(product.getRepaymentMethod()).thenReturn(null);
        when(product.getLoanProductId()).thenReturn(1L);
        when(loanProductMapper.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> loanTermsProvider.getLoanTerms(contract))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("1");
    }
}
