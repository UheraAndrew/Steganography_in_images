package exceptions;

public class ToLongMessageException extends Exception {

    public ToLongMessageException() {
        super();
    }

    public ToLongMessageException(String message) {
        super(message);
    }
}
