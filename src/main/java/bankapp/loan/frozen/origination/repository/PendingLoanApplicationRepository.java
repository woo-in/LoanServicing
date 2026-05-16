package bankapp.loan.frozen.origination.repository;

import bankapp.loan.frozen.origination.model.PendingLoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingLoanApplicationRepository extends JpaRepository <PendingLoanApplication, Long>{ }
