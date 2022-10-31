package bullish.electronic.store.model;

import lombok.Data;

@Data
public class ResponseError {
    private String error;
    public ResponseError(String error){
        this.error = error;
    }
}
