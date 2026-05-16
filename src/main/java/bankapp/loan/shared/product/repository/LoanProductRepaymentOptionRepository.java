package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.LoanProductRepaymentOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanProductRepaymentOptionRepository extends JpaRepository<LoanProductRepaymentOption, Long> {
}
