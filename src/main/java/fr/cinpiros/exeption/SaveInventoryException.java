package fr.cinpiros.exeption;

@Deprecated
public class SaveInventoryException extends Throwable{
    public SaveInventoryException() {
        super();
    }

    public SaveInventoryException(String message) {
        super(message);
    }

    public SaveInventoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveInventoryException(Throwable cause) {
        super(cause);
    }

    protected SaveInventoryException(String message, Throwable cause, boolean enableSuppression, boolean     writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

