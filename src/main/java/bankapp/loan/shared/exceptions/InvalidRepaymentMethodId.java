package bankapp.loan.shared.exceptions;

public class InvalidRepaymentMethodId extends RuntimeException {
    public InvalidRepaymentMethodId(String message) {
        super(message);
    }
}
