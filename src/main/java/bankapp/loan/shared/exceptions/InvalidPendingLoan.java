package bankapp.loan.shared.exceptions;

public class InvalidPendingLoan extends RuntimeException {
    public InvalidPendingLoan(String message) {
        super(message);
    }
}
