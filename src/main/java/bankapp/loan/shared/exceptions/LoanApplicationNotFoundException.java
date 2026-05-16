package bankapp.loan.shared.exceptions;

public class LoanApplicationNotFoundException extends RuntimeException {
    public LoanApplicationNotFoundException(String message) {
        super(message);
    }
}
