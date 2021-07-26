package study.board.api.advice.exception;

public class CNotOwnerException extends RuntimeException {
    public CNotOwnerException() {
    }

    public CNotOwnerException(String message) {
        super(message);
    }

    public CNotOwnerException(String message, Throwable cause) {
        super(message, cause);
    }
}
