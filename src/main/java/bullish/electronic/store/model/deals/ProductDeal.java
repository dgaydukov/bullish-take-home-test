package bullish.electronic.store.model.deals;

import bullish.electronic.store.model.entity.CartItem;
import java.util.List;

/**
 * Universal interface, allows us to create deals with any type of complexity
 */
public interface ProductDeal {
    void applyDeal(List<CartItem> cartItem);
}
