package hello.corebanking.domain.product.repository;

import hello.corebanking.domain.product.entity.RepaymentMethod;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RepaymentMethodMapper {

    void insert(RepaymentMethod repaymentMethod);
    Optional<RepaymentMethod> findById(int repaymentMethodId);
    List<RepaymentMethod> findAll();
}
