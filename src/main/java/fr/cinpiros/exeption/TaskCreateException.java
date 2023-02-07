package fr.cinpiros.exeption;

public class TaskCreateException extends Throwable{
    public TaskCreateException() {
        super();
    }

    public TaskCreateException(String message) {
        super(message);
    }

    public TaskCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskCreateException(Throwable cause) {
        super(cause);
    }

    protected TaskCreateException(String message, Throwable cause, boolean enableSuppression, boolean     writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
