package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.CreditLoanProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreditLoanProductRepository extends JpaRepository<CreditLoanProduct, Long> {

    Optional<CreditLoanProduct> findByLoanProductSlug(String loanProductSlug);


}
