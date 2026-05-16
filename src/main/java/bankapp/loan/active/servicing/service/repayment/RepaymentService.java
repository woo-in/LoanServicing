package bankapp.loan.active.servicing.service.repayment;

import bankapp.account.request.account.AccountTransactionRequest;
import bankapp.account.service.account.AccountService;
import bankapp.loan.shared.exceptions.InvalidRepaymentScheduleException;
import bankapp.loan.active.servicing.dto.RepaymentAllocationInfo;
import bankapp.loan.active.servicing.model.LoanAccount;
import bankapp.loan.active.servicing.model.LoanStatus;
import bankapp.loan.active.servicing.model.RepaymentSchedule;
import bankapp.loan.active.servicing.model.RepaymentStatus;
import bankapp.loan.active.servicing.service.core.LoanAccountService;
import bankapp.loan.active.servicing.service.core.RepaymentScheduleService;
import bankapp.loan.active.servicing.service.core.RepaymentTransactionService;
import bankapp.loan.active.servicing.service.lifecycle.LoanStatusService;
import bankapp.loan.active.servicing.service.lifecycle.RepaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RepaymentService {

    private final LoanAccountService loanAccountService;
    private final RepaymentScheduleService repaymentScheduleService;
    private final RepaymentTransactionService repaymentTransactionService;
    private final RepaymentStatusService repaymentStatusService;
    private final LoanStatusService loanStatusService;
    private final AccountService accountService;

    @Autowired
    public RepaymentService(LoanAccountService loanAccountService,
                                   RepaymentScheduleService repaymentScheduleService,
                                   RepaymentTransactionService repaymentTransactionService,
                                   RepaymentStatusService repaymentStatusService,
                                   LoanStatusService loanStatusService,
                                   AccountService accountService) {
        this.loanAccountService = loanAccountService;
        this.repaymentScheduleService = repaymentScheduleService;
        this.repaymentTransactionService = repaymentTransactionService;
        this.repaymentStatusService = repaymentStatusService;
        this.loanStatusService = loanStatusService;
        this.accountService = accountService;
    }

    @Transactional
    public void processRepayment(Long loanAccountId, BigDecimal repaymentAmount) throws InvalidRepaymentScheduleException {

        if (repaymentAmount == null || repaymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("상환 금액은 0보다 커야 합니다.");
        }

        LoanAccount loanAccount = loanAccountService.getLoanAccount(loanAccountId);
        LoanStatus currentStatus = loanAccount.getLoanStatus();

        switch (currentStatus) {
            case NORMAL -> processNormalRepayment(loanAccountId, repaymentAmount);
            case DELINQUENT -> processDelinquentRepayment(loanAccountId, repaymentAmount);
            case ACCELERATION_NOTICE -> processAccelerationNoticeRepayment(loanAccountId, repaymentAmount);
            case ACCELERATION -> processAccelerationRepayment(loanAccountId, repaymentAmount);
            default -> throw new IllegalStateException(
                    String.format("현재 상태(%s)에서는 상환 처리를 진행할 수 없습니다.", currentStatus));
        }
    }

    private void processNormalRepayment(Long loanAccountId, BigDecimal repaymentAmount) {
        LoanAccount loanAccount = loanAccountService.getLoanAccount(loanAccountId);

        List<RepaymentSchedule> repaymentSchedules = repaymentScheduleService.getRepaymentSchedules(loanAccountId, RepaymentStatus.PENDING);
        if (repaymentSchedules.size() != 1) {
            throw new InvalidRepaymentScheduleException("상환 시점이 아닙니다.");
        }
        RepaymentSchedule schedule = repaymentSchedules.get(0);

        if (repaymentAmount.compareTo(schedule.getTotalAmount()) < 0) {
            processPartialRepayment(loanAccount, schedule, repaymentAmount);
        } else {
            processFullRepayment(loanAccount, schedule);
            // 전액 상환 후 종결 가능 여부 시도 (잔액·미결 스케줄 없으면 TERMINATED)
            loanStatusService.changeLoanStatusToTerminated(loanAccount);
        }
    }

    private void processDelinquentRepayment(Long loanAccountId, BigDecimal repaymentAmount) {
        LoanAccount loanAccount = loanAccountService.getLoanAccount(loanAccountId);

        List<RepaymentSchedule> repaymentSchedules = repaymentScheduleService.getRepaymentSchedules(loanAccountId, RepaymentStatus.OVERDUE);
        if (repaymentSchedules.size() != 1) {
            throw new InvalidRepaymentScheduleException("정상적인 상환 상황이 아닙니다.");
        }
        RepaymentSchedule schedule = repaymentSchedules.get(0);

        if (repaymentAmount.compareTo(schedule.getTotalAmount()) < 0) {
            processPartialRepayment(loanAccount, schedule, repaymentAmount);
        } else {
            processFullRepayment(loanAccount, schedule);
            loanStatusService.changeLoanStatusToNormal(loanAccount);
        }
    }

    private void processAccelerationNoticeRepayment(Long loanAccountId, BigDecimal repaymentAmount) {
        LoanAccount loanAccount = loanAccountService.getLoanAccount(loanAccountId);

        List<RepaymentSchedule> repaymentSchedules = repaymentScheduleService.getRepaymentSchedules(loanAccountId, RepaymentStatus.OVERDUE);
        if (repaymentSchedules.size() != 2) {
            throw new InvalidRepaymentScheduleException("정상적인 상환 상황이 아닙니다.");
        }
        RepaymentSchedule schedule = repaymentSchedules.get(0);

        if (repaymentAmount.compareTo(schedule.getTotalAmount()) < 0) {
            processPartialRepayment(loanAccount, schedule, repaymentAmount);
        } else {
            processFullRepayment(loanAccount, schedule);
            loanStatusService.changeLoanStatusToDelinquent(loanAccount);
        }
    }

    private void processAccelerationRepayment(Long loanAccountId, BigDecimal repaymentAmount) {
        LoanAccount loanAccount = loanAccountService.getLoanAccount(loanAccountId);

        List<RepaymentSchedule> repaymentSchedules = repaymentScheduleService.getRepaymentSchedules(loanAccountId, RepaymentStatus.MERGED);
        if (repaymentSchedules.size() != 1) {
            throw new InvalidRepaymentScheduleException("정상적인 상환 상황이 아닙니다.");
        }
        RepaymentSchedule schedule = repaymentSchedules.get(0);

        if (repaymentAmount.compareTo(schedule.getTotalAmount()) < 0) {
            processPartialRepayment(loanAccount, schedule, repaymentAmount);
        } else {
            processFullRepayment(loanAccount, schedule);
            loanStatusService.changeLoanStatusToTerminated(loanAccount);
        }
    }

    private void processPartialRepayment(LoanAccount loanAccount, RepaymentSchedule schedule, BigDecimal repaymentAmount) {
        executeRepaymentCommon(loanAccount, schedule, repaymentAmount);
    }

    private void processFullRepayment(LoanAccount loanAccount, RepaymentSchedule schedule) {
        BigDecimal fullAmount = schedule.getTotalAmount();
        executeRepaymentCommon(loanAccount, schedule, fullAmount);
        repaymentStatusService.changeRepaymentStatusToComplete(schedule);
    }

    private void executeRepaymentCommon(LoanAccount loanAccount, RepaymentSchedule schedule, BigDecimal amount) {
        RepaymentAllocationInfo allocationInfo = repaymentScheduleService.applyPaymentToSchedule(schedule, amount);

        loanAccountService.updateBalance(loanAccount, allocationInfo.getPrincipalAmount());

        accountService.credit(new AccountTransactionRequest(
                1L,
                allocationInfo.getTotalRepaymentAmount(),
                "상환금 입금"
        ));
        accountService.debit(new AccountTransactionRequest(
                loanAccount.getRepaymentAccount().getAccountId(),
                allocationInfo.getTotalRepaymentAmount(),
                "상환금 출금"
        ));

        repaymentTransactionService.recordRepaymentTransaction(loanAccount, allocationInfo);
    }
}
