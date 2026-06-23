package hello.corebanking.domain.product.repository;

import hello.corebanking.domain.product.entity.InterestType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface InterestTypeMapper {

    void insert(InterestType interestType);
    Optional<InterestType> findById(int interestTypeId);
    List<InterestType> findAll();
}
