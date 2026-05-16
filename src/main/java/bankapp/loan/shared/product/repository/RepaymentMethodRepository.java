package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.RepaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepaymentMethodRepository extends JpaRepository<RepaymentMethod, Long> {
    Optional<RepaymentMethod> findByMethodName(String methodName);
}
