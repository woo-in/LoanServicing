package hello.corebanking.domain.product.repository;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.LoanProduct;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class LoanProductMapperIntegrationTest {

    @Autowired
    private LoanProductMapper loanProductMapper;

    @Test
    @DisplayName("DB에서 가져온 interest_type_id, repayment_method_id가 Enum으로 자동 변환된다")
    void fetchFromDB_mapsIntColumnToEnum() {
        // given: Enum으로 insert (TypeHandler가 FIXED→1, LEVEL_PAYMENT→2 로 변환해서 저장)
        LoanProduct product = new LoanProduct(
                InterestType.FIXED,
                RepaymentMethod.LEVEL_PAYMENT,
                "주택담보대출",
                new BigDecimal("0.02000")
        );
        loanProductMapper.insert(product);

        // when: DB에서 조회 (TypeHandler가 1→FIXED, 2→LEVEL_PAYMENT 로 역변환)
        LoanProduct found = loanProductMapper.findById(product.getLoanProductId()).orElseThrow();

        // then
        assertThat(found.getInterestType()).isEqualTo(InterestType.FIXED);
        assertThat(found.getInterestType().getId()).isEqualTo(1);

        assertThat(found.getRepaymentMethod()).isEqualTo(RepaymentMethod.LEVEL_PAYMENT);
        assertThat(found.getRepaymentMethod().getId()).isEqualTo(2);
    }

    @Test
    @DisplayName("서로 다른 Enum 조합으로 저장해도 각각 올바른 Enum으로 조회된다")
    void fetchFromDB_differentEnumCombinations() {
        LoanProduct p1 = new LoanProduct(InterestType.VARIABLE, RepaymentMethod.BULLET, "신용대출A", new BigDecimal("0.03000"));
        LoanProduct p2 = new LoanProduct(InterestType.FIXED, RepaymentMethod.EQUAL_PRINCIPAL, "신용대출B", new BigDecimal("0.01500"));
        loanProductMapper.insert(p1);
        loanProductMapper.insert(p2);

        LoanProduct found1 = loanProductMapper.findById(p1.getLoanProductId()).orElseThrow();
        LoanProduct found2 = loanProductMapper.findById(p2.getLoanProductId()).orElseThrow();

        assertThat(found1.getInterestType()).isEqualTo(InterestType.VARIABLE);       // id=2
        assertThat(found1.getRepaymentMethod()).isEqualTo(RepaymentMethod.BULLET);   // id=1

        assertThat(found2.getInterestType()).isEqualTo(InterestType.FIXED);                  // id=1
        assertThat(found2.getRepaymentMethod()).isEqualTo(RepaymentMethod.EQUAL_PRINCIPAL);  // id=3
    }
}
