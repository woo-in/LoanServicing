package hello.corebanking.global.exception;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException(String message) {
        super(message);
    }
}
