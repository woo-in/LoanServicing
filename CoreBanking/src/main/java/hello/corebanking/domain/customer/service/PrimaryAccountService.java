package hello.corebanking.domain.customer.service;

import hello.corebanking.domain.customer.dto.OpenPrimaryAccountRequest;
import hello.corebanking.domain.customer.entity.PrimaryAccount;
import hello.corebanking.domain.customer.repository.PrimaryAccountMapper;
import hello.corebanking.domain.customer.service.component.PrimaryAccountOpenValidator;
import hello.corebanking.global.exception.NotFoundException;
import hello.corebanking.global.util.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrimaryAccountService {

    private final PrimaryAccountMapper primaryAccountMapper;
    private final PrimaryAccountOpenValidator validator;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public PrimaryAccount open(OpenPrimaryAccountRequest request) {
        validator.validate(request);

        PrimaryAccount account = new PrimaryAccount(
                request.getMemberId(),
                accountNumberGenerator.generate()
        );
        primaryAccountMapper.insert(account);
        return account;
    }

    @Transactional(readOnly = true)
    public PrimaryAccount findByMemberId(long memberId) {
        return primaryAccountMapper.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("주계좌를 찾을 수 없습니다. memberId=" + memberId));
    }
}

