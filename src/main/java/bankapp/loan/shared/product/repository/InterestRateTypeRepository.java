package bankapp.loan.shared.product.repository;

import bankapp.loan.shared.product.model.InterestRateType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRateTypeRepository extends JpaRepository<InterestRateType, Long> {
    Optional<InterestRateType> findByTypeName(String typeName);
}
