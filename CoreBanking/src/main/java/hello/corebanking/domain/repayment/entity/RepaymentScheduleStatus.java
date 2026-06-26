package hello.corebanking.domain.repayment.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum RepaymentScheduleStatus {
    SCHEDULED(1), DUE(2), PAID(3), OVERDUE(4);

    private final int id;

    RepaymentScheduleStatus(int id) { this.id = id; }

    public int getId() { return id; }

    public static RepaymentScheduleStatus fromId(int id) {
        for (RepaymentScheduleStatus status : values()) {
            if (status.id == id) return status;
        }
        throw new InvalidEnumIdException("Unknown RepaymentScheduleStatus id: " + id);
    }
}
