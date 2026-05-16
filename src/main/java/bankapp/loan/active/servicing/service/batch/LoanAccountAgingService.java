package bankapp.loan.active.servicing.service.batch;

import bankapp.loan.active.servicing.model.LoanAccount;
import bankapp.loan.active.servicing.model.LoanStatus;
import bankapp.loan.active.servicing.service.core.LoanAccountService;
import bankapp.loan.active.servicing.service.lifecycle.LoanStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LoanAccountAgingService {

    private final LoanAccountService loanAccountService;
    private final LoanStatusService loanStatusService;

    @Autowired
    public LoanAccountAgingService(LoanAccountService loanAccountService,
                                          LoanStatusService loanStatusService) {
        this.loanAccountService = loanAccountService;
        this.loanStatusService = loanStatusService;
    }

    @Transactional
    public void transitionLoanStatuses() {
        // 정상화 우선 처리 후, 악화 방향으로 순차 전환
        transitionToNormal();
        transitionToDelinquent();
        transitionToAccelerationNotice();
        transitionToAcceleration();
        transitionToTerminated();
    }

    private void transitionToNormal() {
        List<LoanAccount> candidates = loanAccountService.findCandidateAccountsForStatus(LoanStatus.NORMAL);
        for (LoanAccount account : candidates) {
            try {
                loanStatusService.changeLoanStatusToNormal(account);
            } catch (Exception ignored) {
            }
        }
    }

    private void transitionToDelinquent() {
        List<LoanAccount> candidates = loanAccountService.findCandidateAccountsForStatus(LoanStatus.DELINQUENT);
        for (LoanAccount account : candidates) {
            try {
                loanStatusService.changeLoanStatusToDelinquent(account);
            } catch (Exception ignored) {
            }
        }
    }

    private void transitionToAccelerationNotice() {
        List<LoanAccount> candidates = loanAccountService.findCandidateAccountsForStatus(LoanStatus.ACCELERATION_NOTICE);
        for (LoanAccount account : candidates) {
            try {
                loanStatusService.changeLoanStatusToAccelerationNotice(account);
            } catch (Exception ignored) {
            }
        }
    }

    private void transitionToAcceleration() {
        List<LoanAccount> candidates = loanAccountService.findCandidateAccountsForStatus(LoanStatus.ACCELERATION);
        for (LoanAccount account : candidates) {
            try {
                loanStatusService.changeLoanStatusToAcceleration(account);
            } catch (Exception ignored) {
            }
        }
    }

    private void transitionToTerminated() {
        List<LoanAccount> candidates = loanAccountService.findCandidateAccountsForStatus(LoanStatus.TERMINATED);
        for (LoanAccount account : candidates) {
            try {
                loanStatusService.changeLoanStatusToTerminated(account);
            } catch (Exception ignored) {
            }
        }
    }
}
