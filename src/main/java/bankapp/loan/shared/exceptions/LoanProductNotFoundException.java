package bankapp.loan.shared.exceptions;

public class LoanProductNotFoundException extends RuntimeException {
    public LoanProductNotFoundException(String message) {
        super(message);
    }
}
