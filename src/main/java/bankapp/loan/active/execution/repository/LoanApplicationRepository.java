package bankapp.loan.active.execution.repository;

import bankapp.loan.active.execution.model.ApplicationStatus;
import bankapp.loan.active.execution.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByApplicationStatusOrderByCreatedAtDesc(ApplicationStatus applicationStatus);
    List<LoanApplication> findAllByMember_MemberIdOrderByCreatedAtDesc(Long memberId);
    Optional<LoanApplication> findByLoanApplicationIdAndApplicationStatus(Long loanApplicationId, ApplicationStatus applicationStatus);
}
