package bankapp.loan.active.servicing.repository;

import bankapp.loan.active.servicing.model.LoanStatus;
import bankapp.loan.active.servicing.model.RepaymentSchedule;
import bankapp.loan.active.servicing.model.RepaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {
    List<RepaymentSchedule> findByStatusAndDueDateLessThanEqual(RepaymentStatus status, LocalDate date);
    List<RepaymentSchedule> findByLoanAccount_AccountIdAndStatusOrderByDueDateAsc(Long accountId, RepaymentStatus status);
    List<RepaymentSchedule> findByLoanAccount_LoanStatusAndStatus(LoanStatus loanStatus, RepaymentStatus status);
    List<RepaymentSchedule> findByLoanAccount_LoanStatusAndStatusAndDueDateLessThanEqual(LoanStatus loanStatus, RepaymentStatus status, LocalDate date);
    List<RepaymentSchedule> findByLoanAccount_LoanStatusInAndStatusAndDueDateLessThanEqual(List<LoanStatus> loanStatuses, RepaymentStatus status, LocalDate date);
    List<RepaymentSchedule> findByStatus(RepaymentStatus status);
}
