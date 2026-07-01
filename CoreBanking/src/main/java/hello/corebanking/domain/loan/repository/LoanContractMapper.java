package hello.corebanking.domain.loan.repository;

import hello.corebanking.domain.loan.entity.LoanContract;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LoanContractMapper {

    Optional<LoanContract> findById(long loanContractId);

    Optional<LoanContract> findByLoanAccountId(long loanAccountId);
}
