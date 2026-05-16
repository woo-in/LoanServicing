package bankapp.loan.frozen.origination.repository;

import bankapp.loan.frozen.origination.model.ExistingLoan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExistingLoanRepository extends JpaRepository <ExistingLoan, Long>{ }
