package hello.corebanking.domain.loan.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum LoanStatus {
    NORMAL(1), DELINQUENT(2), PAID_OFF(3), ACCELERATED(4);

    private final int id;

    LoanStatus(int id) { this.id = id; }

    public int getId() { return id; }

    public static LoanStatus fromId(int id) {
        for (LoanStatus status : values()) {
            if (status.id == id) return status;
        }
        throw new InvalidEnumIdException("Unknown LoanStatus id: " + id);
    }
}
