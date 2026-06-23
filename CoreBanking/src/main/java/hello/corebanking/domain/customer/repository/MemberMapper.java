package hello.corebanking.domain.customer.repository;

import hello.corebanking.domain.customer.entity.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface MemberMapper {

    void insert(Member member);
    Optional<Member> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
