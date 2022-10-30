package bullish.electronic.store.exception;

public class ProductNotFoundException extends RequestValidationException {
    public ProductNotFoundException(String msg){
        super(msg);
    }
}
