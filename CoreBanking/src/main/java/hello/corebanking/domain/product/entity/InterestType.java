package hello.corebanking.domain.product.entity;

import hello.corebanking.global.exception.InvalidEnumIdException;

public enum InterestType {
    FIXED(1), VARIABLE(2);

    private final int id;

    InterestType(int id) { this.id = id; }

    public int getId() { return id; }

    public static InterestType fromId(int id) {
        for (InterestType type : values()) {
            if (type.id == id) return type;
        }
        throw new InvalidEnumIdException("Unknown InterestType id: " + id);
    }
}
