package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.LoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanProductRepository extends JpaRepository<LoanProduct, Long> {

    Optional<LoanProduct> findByLoanProductSlug(String loanProductSlug);

}
