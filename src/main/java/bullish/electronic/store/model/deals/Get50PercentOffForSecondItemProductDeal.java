package bullish.electronic.store.model.deals;

import bullish.electronic.store.model.entity.CartItem;

import java.util.List;

public class Get50PercentOffForSecondItemProductDeal implements ProductDeal {
    @Override
    public void applyDeal(List<CartItem> items) {
        if (items != null) {
            items.forEach(item -> {
                if (item.getQuantity() > 1) {
                    double totalPrice;
                    if (item.getQuantity() % 2 == 0) {
                        totalPrice = item.getTotalPrice() * 0.75;
                    } else {
                        totalPrice = (item.getTotalPrice() - item.getPrice()) * 0.75 + item.getPrice();
                    }
                    item.setTotalPrice(totalPrice);
                }
            });
        }
    }
}
