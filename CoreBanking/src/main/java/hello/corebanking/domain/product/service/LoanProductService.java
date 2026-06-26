package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.entity.LoanProduct;
import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.LoanProductMapper;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanProductService {

    private final LoanProductMapper loanProductMapper;

    @Transactional
    public LoanProduct register(int interestTypeId, int repaymentMethodId,
                                String name, BigDecimal additionalRate) {
        InterestType interestType = InterestType.fromId(interestTypeId);
        RepaymentMethod repaymentMethod = RepaymentMethod.fromId(repaymentMethodId);

        LoanProduct loanProduct = new LoanProduct(interestType, repaymentMethod, name, additionalRate);
        loanProductMapper.insert(loanProduct);
        return loanProduct;
    }

    @Transactional(readOnly = true)
    public LoanProduct findById(long loanProductId) {
        return loanProductMapper.findById(loanProductId)
                .orElseThrow(() -> new NotFoundException("대출상품을 찾을 수 없습니다. id=" + loanProductId));
    }

    @Transactional(readOnly = true)
    public List<LoanProduct> findAll() {
        return loanProductMapper.findAll();
    }
}
