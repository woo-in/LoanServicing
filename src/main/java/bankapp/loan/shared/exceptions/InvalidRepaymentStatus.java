package bankapp.loan.shared.exceptions;

public class InvalidRepaymentStatus extends RuntimeException {
    public InvalidRepaymentStatus(String message) {
        super(message);
    }
}
