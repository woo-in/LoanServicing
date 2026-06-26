package hello.corebanking.domain.repayment.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum DelinquencyReason {
    BULLET_INTEREST_SHORT(1),
    BULLET_INTEREST_LONG(2),
    BULLET_INTEREST_LONG_PROTECTED(3),
    BULLET_PRINCIPAL(4),
    INSTALLMENT_SINGLE(5),
    INSTALLMENT_CONSECUTIVE(6),
    INSTALLMENT_CONSECUTIVE_PROTECTED(7);

    private final int id;

    DelinquencyReason(int id) { this.id = id; }

    public int getId() { return id; }

    public static DelinquencyReason fromId(int id) {
        for (DelinquencyReason reason : values()) {
            if (reason.id == id) return reason;
        }
        throw new InvalidEnumIdException("Unknown DelinquencyReason id: " + id);
    }
}
