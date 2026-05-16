package bankapp.loan.shared.exceptions;

public class InvalidRepaymentStatusException extends RuntimeException {
    public InvalidRepaymentStatusException(String message) {
        super(message);
    }
}
