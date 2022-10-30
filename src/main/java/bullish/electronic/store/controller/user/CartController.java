package bullish.electronic.store.controller.user;

import bullish.electronic.store.model.Receipt;
import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.service.UserCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user/cart")
public class CartController {
    @Autowired
    private UserCartService userCartService;

    @PostMapping("product")
    public CartItem addProduct(@RequestBody CartItem cartItem){
        return userCartService.addProduct(cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity());
    }
    @DeleteMapping("product")
    public void deleteProduct(@RequestBody CartItem cartItem){
        userCartService.deleteProduct(cartItem.getUserId(), cartItem.getProductId());
    }

    @PostMapping("receipt")
    public Receipt calculateReceipt(@RequestBody CartItem cartItem){
        final int userId = cartItem.getUserId();
        Receipt receipt = new Receipt();
        receipt.setUserId(userId);
        receipt.setTotalPrice(userCartService.calculateReceipt(userId));
        return receipt;
    }
}
