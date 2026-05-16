package bankapp.loan.active.servicing.repository;

import bankapp.loan.active.servicing.model.RepaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentTransactionRepository extends JpaRepository<RepaymentTransaction, Long> {
}
