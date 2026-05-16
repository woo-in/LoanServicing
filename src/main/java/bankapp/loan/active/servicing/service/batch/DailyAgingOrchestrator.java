package bankapp.loan.active.servicing.service.batch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DailyAgingOrchestrator {

    private final RepaymentScheduleAgingService repaymentScheduleAgingService;
    private final LoanAccountAgingService loanAccountAgingService;
    private final OverdueBalanceUpdateService overdueBalanceUpdateService;

    @Autowired
    public DailyAgingOrchestrator(RepaymentScheduleAgingService repaymentScheduleAgingService,
                                         LoanAccountAgingService loanAccountAgingService,
                                         OverdueBalanceUpdateService overdueBalanceUpdateService) {
        this.repaymentScheduleAgingService = repaymentScheduleAgingService;
        this.loanAccountAgingService = loanAccountAgingService;
        this.overdueBalanceUpdateService = overdueBalanceUpdateService;
    }

    @Transactional
    public void processDailyAging(LocalDate targetDate) {
        repaymentScheduleAgingService.transitionRepaymentStatuses(targetDate);
        repaymentScheduleAgingService.mergeBalancesForAcceleration();
        loanAccountAgingService.transitionLoanStatuses();
        overdueBalanceUpdateService.updateOverdueBalances();
    }
}
