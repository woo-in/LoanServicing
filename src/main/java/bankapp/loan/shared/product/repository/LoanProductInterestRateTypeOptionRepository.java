package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.LoanProductInterestRateTypeOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanProductInterestRateTypeOptionRepository extends JpaRepository<LoanProductInterestRateTypeOption, Long> {
}
