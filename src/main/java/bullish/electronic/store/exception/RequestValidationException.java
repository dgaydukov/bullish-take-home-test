package bullish.electronic.store.exception;

public class RequestValidationException extends RuntimeException {
    public RequestValidationException(String msg){
        super(msg);
    }
}
