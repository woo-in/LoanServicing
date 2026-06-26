package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.LoanProduct;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.LoanProductMapper;
import hello.corebanking.global.exception.InvalidEnumIdException;
import hello.corebanking.global.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanProductServiceTest {

    @Mock
    private LoanProductMapper loanProductMapper;

    @InjectMocks
    private LoanProductService loanProductService;

    @Test
    @DisplayName("유효한 id로 대출상품 등록 시 Enum으로 변환 후 저장한다")
    void register() {
        LoanProduct result = loanProductService.register(1, 2, "주택담보대출", new BigDecimal("0.02000"));

        assertThat(result.getName()).isEqualTo("주택담보대출");
        assertThat(result.getInterestType()).isEqualTo(InterestType.FIXED);
        assertThat(result.getRepaymentMethod()).isEqualTo(RepaymentMethod.LEVEL_PAYMENT);
        verify(loanProductMapper).insert(any(LoanProduct.class));
    }

    @Test
    @DisplayName("존재하지 않는 interestTypeId 로 등록 시 InvalidEnumIdException 을 던진다")
    void register_invalidInterestTypeId() {
        assertThatThrownBy(() -> loanProductService.register(99, 1, "주택담보대출", new BigDecimal("0.02000")))
                .isInstanceOf(InvalidEnumIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("존재하지 않는 repaymentMethodId 로 등록 시 InvalidEnumIdException 을 던진다")
    void register_invalidRepaymentMethodId() {
        assertThatThrownBy(() -> loanProductService.register(1, 99, "주택담보대출", new BigDecimal("0.02000")))
                .isInstanceOf(InvalidEnumIdException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("존재하는 id 조회 시 대출상품을 반환한다")
    void findById_found() {
        LoanProduct loanProduct = new LoanProduct(
                InterestType.FIXED, RepaymentMethod.LEVEL_PAYMENT,
                "주택담보대출", new BigDecimal("0.02000")
        );
        when(loanProductMapper.findById(1L)).thenReturn(Optional.of(loanProduct));

        LoanProduct result = loanProductService.findById(1L);

        assertThat(result.getName()).isEqualTo("주택담보대출");
        assertThat(result.getInterestType()).isEqualTo(InterestType.FIXED);
    }

    @Test
    @DisplayName("존재하지 않는 id 조회 시 NotFoundException 을 던진다")
    void findById_notFound() {
        when(loanProductMapper.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loanProductService.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("전체 조회 시 mapper 에서 받은 목록을 반환한다")
    void findAll() {
        List<LoanProduct> list = List.of(
                new LoanProduct(InterestType.FIXED, RepaymentMethod.LEVEL_PAYMENT, "주택담보대출", new BigDecimal("0.02000")),
                new LoanProduct(InterestType.VARIABLE, RepaymentMethod.BULLET, "신용대출", new BigDecimal("0.03000"))
        );
        when(loanProductMapper.findAll()).thenReturn(list);

        List<LoanProduct> result = loanProductService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(LoanProduct::getName)
                .containsExactly("주택담보대출", "신용대출");
    }
}
