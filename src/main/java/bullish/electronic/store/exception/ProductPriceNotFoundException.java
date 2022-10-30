package bullish.electronic.store.exception;

public class ProductPriceNotFoundException extends RequestValidationException {
    public ProductPriceNotFoundException(String msg){
        super(msg);
    }
}
