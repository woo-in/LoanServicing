package hello.corebanking.domain.product.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum RepaymentMethod {
    BULLET(1), LEVEL_PAYMENT(2), EQUAL_PRINCIPAL(3);

    private final int id;

    RepaymentMethod(int id) { this.id = id; }

    public int getId() { return id; }

    public static RepaymentMethod fromId(int id) {
        for (RepaymentMethod method : values()) {
            if (method.id == id) return method;
        }
        throw new InvalidEnumIdException("Unknown RepaymentMethod id: " + id);
    }
}
