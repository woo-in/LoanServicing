package hello.corebanking.domain.product.service;

import hello.corebanking.domain.product.entity.InterestType;
import hello.corebanking.domain.product.repository.InterestTypeMapper;
import hello.corebanking.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestTypeService {

    private final InterestTypeMapper interestTypeMapper;

    @Transactional
    public InterestType register(String name) {
        InterestType interestType = new InterestType(name);
        interestTypeMapper.insert(interestType);
        return interestType;
    }

    @Transactional(readOnly = true)
    public InterestType findById(int interestTypeId) {
        return interestTypeMapper.findById(interestTypeId)
                .orElseThrow(() -> new NotFoundException("이자종류를 찾을 수 없습니다. id=" + interestTypeId));
    }

    @Transactional(readOnly = true)
    public List<InterestType> findAll() {
        return interestTypeMapper.findAll();
    }
}
