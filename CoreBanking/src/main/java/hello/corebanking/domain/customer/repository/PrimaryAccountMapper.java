package hello.corebanking.domain.customer.repository;

import hello.corebanking.domain.customer.entity.PrimaryAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PrimaryAccountMapper {

    void insert(PrimaryAccount primaryAccount);
    Optional<PrimaryAccount> findByMemberId(long memberId);
    boolean existsByMemberId(long memberId);
}
