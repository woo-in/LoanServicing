package bankapp.loan.active.servicing.service.batch;

import bankapp.loan.active.servicing.model.LoanStatus;
import bankapp.loan.active.servicing.model.RepaymentSchedule;
import bankapp.loan.active.servicing.model.RepaymentStatus;
import bankapp.loan.active.servicing.service.core.RepaymentScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OverdueBalanceUpdateService {

    private final RepaymentScheduleService repaymentScheduleService;

    @Autowired
    public OverdueBalanceUpdateService(RepaymentScheduleService repaymentScheduleService) {
        this.repaymentScheduleService = repaymentScheduleService;
    }

    @Transactional
    public void updateOverdueBalances() {
        updateBalanceForDelinquent();
        updateBalanceForAccelerationNotice();
        updateBalanceForAcceleration();
    }

    private void updateBalanceForDelinquent() {
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanAndRepaymentStatus(
                LoanStatus.DELINQUENT, RepaymentStatus.OVERDUE);
        for (RepaymentSchedule schedule : schedules) {
            repaymentScheduleService.updateDailyDelinquent(schedule);
        }
    }

    private void updateBalanceForAccelerationNotice() {
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanAndRepaymentStatus(
                LoanStatus.ACCELERATION_NOTICE, RepaymentStatus.CRITICAL_OVERDUE);
        for (RepaymentSchedule schedule : schedules) {
            repaymentScheduleService.updateDailyAcceleration(schedule);
        }
    }

    private void updateBalanceForAcceleration() {
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanAndRepaymentStatus(
                LoanStatus.ACCELERATION, RepaymentStatus.ACCELERATED);
        for (RepaymentSchedule schedule : schedules) {
            repaymentScheduleService.updateDailyAcceleration(schedule);
        }
    }
}
