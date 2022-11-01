package bullish.electronic.store.model.deals;

import bullish.electronic.store.model.entity.CartItem;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;

class Get50PercentOffForSameItemProductDealTest {
    @Test
    public void dealTest(){
        ProductDeal deal = new Get50PercentOffForSecondItemProductDeal();
        CartItem item1 = new CartItem();
        item1.setPrice(100);
        item1.setQuantity(2);
        item1.setTotalPrice(200);
        CartItem item2 = new CartItem();
        item2.setPrice(50);
        item2.setQuantity(3);
        item2.setTotalPrice(150);
        List<CartItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);
        // total price = (100 + 100/2) + (50 + 50/2 + 50)
        deal.applyDeal(items);
        double totalPrice = 0;
        for (CartItem item: items){
            totalPrice += item.getTotalPrice();
        }
        Assert.isTrue(totalPrice == 275, "Failed to apply deal correctly");
    }
}