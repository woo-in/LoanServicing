package bankapp.loan.active.servicing.service.batch;

import bankapp.loan.active.servicing.model.LoanStatus;
import bankapp.loan.active.servicing.model.RepaymentSchedule;
import bankapp.loan.active.servicing.model.RepaymentStatus;
import bankapp.loan.active.servicing.service.core.RepaymentScheduleService;
import bankapp.loan.active.servicing.service.lifecycle.RepaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepaymentScheduleAgingService {

    private static final int DAYS_BEFORE_FOR_PENDING = 5;
    private static final int MONTHS_AFTER_FOR_CRITICAL_OVERDUE = 1;
    private static final int MONTHS_AFTER_FOR_ACCELERATED = 2;
    private static final int MONTHS_AFTER_FOR_MERGED = 1;

    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentStatusService repaymentStatusService;

    @Autowired
    public RepaymentScheduleAgingService(RepaymentScheduleService repaymentScheduleService,
                                                RepaymentStatusService repaymentStatusService) {
        this.repaymentScheduleService = repaymentScheduleService;
        this.repaymentStatusService = repaymentStatusService;
    }

    @Transactional
    public void transitionRepaymentStatuses(LocalDate targetDate) {
        transitionStatusToPending(targetDate);
        transitionStatusToOverdue(targetDate);
        transitionStatusToCriticalOverdue(targetDate);
        transitionStatusToAccelerated(targetDate);
        transitionStatusToMerged(targetDate);
    }

    @Transactional
    public void mergeBalancesForAcceleration() {
        List<RepaymentSchedule> acceleratedSchedules = repaymentScheduleService.getRepaymentSchedules(RepaymentStatus.ACCELERATED);
        for (RepaymentSchedule accelerated : acceleratedSchedules) {
            List<RepaymentSchedule> mergedSchedules = repaymentScheduleService.getRepaymentSchedules(
                    accelerated.getLoanAccount().getAccountId(), RepaymentStatus.MERGED);
            repaymentScheduleService.mergeBalance(accelerated, mergedSchedules);
        }
    }

    private void transitionStatusToPending(LocalDate targetDate) {
        List<LoanStatus> targetLoanStatuses = List.of(
                LoanStatus.NORMAL, LoanStatus.DELINQUENT, LoanStatus.ACCELERATION_NOTICE);
        LocalDate criteriaDate = targetDate.plusDays(DAYS_BEFORE_FOR_PENDING);
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.PLANNED, criteriaDate);
        for (RepaymentSchedule schedule : schedules) {
            repaymentScheduleService.updateAmount(schedule);
            repaymentStatusService.changeRepaymentStatusToPending(schedule, targetDate);
        }
    }

    private void transitionStatusToOverdue(LocalDate targetDate) {
        List<LoanStatus> targetLoanStatuses = List.of(LoanStatus.NORMAL, LoanStatus.DELINQUENT);
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.PENDING, targetDate);
        for (RepaymentSchedule schedule : schedules) {
            repaymentStatusService.changeRepaymentStatusToOverdue(schedule, targetDate);
        }
    }

    private void transitionStatusToCriticalOverdue(LocalDate targetDate) {
        List<LoanStatus> targetLoanStatuses = List.of(LoanStatus.DELINQUENT);
        LocalDate criteriaDate = targetDate.minusMonths(MONTHS_AFTER_FOR_CRITICAL_OVERDUE);
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.OVERDUE, criteriaDate);
        for (RepaymentSchedule schedule : schedules) {
            repaymentStatusService.changeRepaymentStatusToCriticalOverdue(schedule, targetDate);
        }
    }

    private void transitionStatusToAccelerated(LocalDate targetDate) {
        List<LoanStatus> targetLoanStatuses = List.of(LoanStatus.ACCELERATION_NOTICE);
        LocalDate criteriaDate = targetDate.minusMonths(MONTHS_AFTER_FOR_ACCELERATED);
        List<RepaymentSchedule> schedules = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.CRITICAL_OVERDUE, criteriaDate);
        for (RepaymentSchedule schedule : schedules) {
            repaymentStatusService.changeRepaymentStatusToAccelerated(schedule, targetDate);
        }
    }

    private void transitionStatusToMerged(LocalDate targetDate) {
        List<LoanStatus> targetLoanStatuses = List.of(LoanStatus.ACCELERATION_NOTICE);

        // 조건 A: OVERDUE + 1개월 경과
        LocalDate criteriaDateA = targetDate.minusMonths(MONTHS_AFTER_FOR_MERGED);
        List<RepaymentSchedule> schedulesA = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.OVERDUE, criteriaDateA);
        for (RepaymentSchedule schedule : schedulesA) {
            repaymentStatusService.changeRepaymentStatusToMerged(schedule, targetDate);
        }

        // 조건 B: PENDING + 만기일 경과
        List<RepaymentSchedule> schedulesB = repaymentScheduleService.findSchedulesByLoanStatusesAndRepaymentStatusAndDueDate(
                targetLoanStatuses, RepaymentStatus.PENDING, targetDate);
        for (RepaymentSchedule schedule : schedulesB) {
            repaymentStatusService.changeRepaymentStatusToMerged(schedule, targetDate);
        }
    }
}
