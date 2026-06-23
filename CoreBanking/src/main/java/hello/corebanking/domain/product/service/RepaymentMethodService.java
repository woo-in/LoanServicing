package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import hello.corebanking.domain.product.repository.RepaymentMethodMapper;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepaymentMethodService {

    private final RepaymentMethodMapper repaymentMethodMapper;

    @Transactional
    public RepaymentMethod register(String name) {
        RepaymentMethod repaymentMethod = new RepaymentMethod(name);
        repaymentMethodMapper.insert(repaymentMethod);
        return repaymentMethod;
    }

    @Transactional(readOnly = true)
    public RepaymentMethod findById(int repaymentMethodId) {
        return repaymentMethodMapper.findById(repaymentMethodId)
                .orElseThrow(() -> new NotFoundException("상환방법을 찾을 수 없습니다. id=" + repaymentMethodId));
    }

    @Transactional(readOnly = true)
    public List<RepaymentMethod> findAll() {
        return repaymentMethodMapper.findAll();
    }
}
