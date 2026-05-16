package bankapp.loan.shared.exceptions;

public class InvalidRepaymentScheduleException extends RuntimeException {
    public InvalidRepaymentScheduleException(String message) {
        super(message);
    }
}
