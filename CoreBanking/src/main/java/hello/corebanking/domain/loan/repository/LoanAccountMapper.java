package hello.corebanking.domain.loan.repository;

import hello.corebanking.domain.loan.entity.LoanAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface LoanAccountMapper {

    Optional<LoanAccount> findById(long loanAccountId);
}
