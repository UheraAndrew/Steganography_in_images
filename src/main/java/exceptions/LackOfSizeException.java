package exceptions;

public class LackOfSizeException extends Exception {

    public LackOfSizeException() {
        super();
    }

    public LackOfSizeException(String message) {
        super(message);
    }
}