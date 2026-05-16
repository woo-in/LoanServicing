package bankapp.loan.shared.exceptions;

public class InvalidLoanAccountException extends RuntimeException {
    public InvalidLoanAccountException(String message) {
        super(message);
    }
}
