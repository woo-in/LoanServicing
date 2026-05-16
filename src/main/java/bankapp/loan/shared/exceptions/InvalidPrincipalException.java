package bankapp.loan.shared.exceptions;

public class InvalidPrincipalException extends RuntimeException {
    public InvalidPrincipalException(String message) {
        super(message);
    }
}
