package bankapp.loan.shared.exceptions;

public class OverRepaymentException extends RuntimeException {
    public OverRepaymentException(String message) {
        super(message);
    }
}
