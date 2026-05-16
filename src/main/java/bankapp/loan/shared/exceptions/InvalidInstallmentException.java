package bankapp.loan.shared.exceptions;

public class InvalidInstallmentException extends RuntimeException {
    public InvalidInstallmentException(String message) {
        super(message);
    }
}
