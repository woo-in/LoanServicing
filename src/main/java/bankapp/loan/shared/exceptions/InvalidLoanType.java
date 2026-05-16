package bankapp.loan.shared.exceptions;

public class InvalidLoanType extends RuntimeException {
    public InvalidLoanType(String message) {
        super(message);
    }
}
