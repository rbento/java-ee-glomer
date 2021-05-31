package glomer.mongodb.exception;

public class DocumentHasChildrenException extends Exception {

    private static final long serialVersionUID = 1L;

    public DocumentHasChildrenException() {
    }

    public DocumentHasChildrenException(String message) {
        super(message);
    }

    public DocumentHasChildrenException(Throwable cause) {
        super(cause);
    }

    public DocumentHasChildrenException(String message, Throwable cause) {
        super(message, cause);
    }
}
