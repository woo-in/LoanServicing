package bankapp.loan.active.execution.service;

import bankapp.loan.active.servicing.model.LoanAccount;
import bankapp.loan.active.execution.model.LoanApplication;
import bankapp.loan.active.execution.model.LoanContract;
import bankapp.loan.active.execution.repository.LoanContractRepository;
import bankapp.loan.frozen.origination.web.response.ExistingLoanResponse;
import bankapp.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DefaultLoanContractService implements LoanContractService {

    private final LoanContractRepository loanContractRepository;

    @Autowired
    public DefaultLoanContractService(LoanContractRepository loanContractRepository) {
        this.loanContractRepository = loanContractRepository;
    }


    @Override
    @Transactional
    public LoanContract saveLoanContract(LoanApplication loanApplication, LoanAccount loanAccount) {
         LoanContract loanContract = LoanContract.from(loanApplication , loanAccount);
        return loanContractRepository.save(loanContract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanContract> findAllByMember(Member member) {
        return loanContractRepository.findAllByMember(member);
    }


    @Override
    public List<ExistingLoanResponse> findAllContractResponsesByMember(Member member) {
        return loanContractRepository.findAllByMember(member).stream()
                .map(ExistingLoanResponse::from)
                .collect(Collectors.toList());
    }

}