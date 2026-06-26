package hello.corebanking.domain.repayment.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum DelinquencyStatus {
    ACTIVE(1), CURED(2);

    private final int id;

    DelinquencyStatus(int id) { this.id = id; }

    public int getId() { return id; }

    public static DelinquencyStatus fromId(int id) {
        for (DelinquencyStatus status : values()) {
            if (status.id == id) return status;
        }
        throw new InvalidEnumIdException("Unknown DelinquencyStatus id: " + id);
    }
}
