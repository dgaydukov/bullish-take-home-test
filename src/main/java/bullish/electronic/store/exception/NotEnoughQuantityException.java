package bullish.electronic.store.exception;

public class NotEnoughQuantityException extends RequestValidationException {
    public NotEnoughQuantityException(String msg){
        super(msg);
    }
}
