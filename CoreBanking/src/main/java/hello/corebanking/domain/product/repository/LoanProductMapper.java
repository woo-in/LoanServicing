package hello.corebanking.domain.product.repository;

import hello.corebanking.domain.product.entity.LoanProduct;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface LoanProductMapper {

    void insert(LoanProduct loanProduct);
    Optional<LoanProduct> findById(long loanProductId);
    List<LoanProduct> findAll();
}
