package bankapp.loan.shared.exceptions;

public class InvalidInterestRate extends RuntimeException {
    public InvalidInterestRate(String message) {
        super(message);
    }
}
