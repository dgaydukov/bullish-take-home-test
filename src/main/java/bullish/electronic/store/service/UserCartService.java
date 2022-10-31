package bullish.electronic.store.service;

import bullish.electronic.store.exception.NotEnoughQuantityException;
import bullish.electronic.store.exception.ProductNotFoundException;
import bullish.electronic.store.exception.ProductPriceNotFoundException;
import bullish.electronic.store.model.entity.Deal;
import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.repository.ProductPriceRepository;
import bullish.electronic.store.repository.ProductRepository;
import bullish.electronic.store.repository.UserCartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserCartService {
    private final UserCartRepository userCartRepository;
    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final DealService dealService;

    public CartItem addProduct(int userId, int productId, int quantity){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("No product for productId="+productId));
        ProductPrice price = productPriceRepository
                .findOneByProductIdOrderByIdDesc(productId)
                .orElseThrow(()-> new ProductPriceNotFoundException("No product price for productId="+productId));
        CartItem cartItem = userCartRepository.findByUserIdAndProductId(userId, productId)
                .orElse(new CartItem());
        final int newQuantity = quantity - cartItem.getQuantity();
        // update quantity
        if (product.getQuantity() < newQuantity){
            throw new NotEnoughQuantityException("Not enough products, requestedQuantity="+newQuantity+", availableQuantity="
                    + product.getQuantity() + ", for productId=" + productId);
        }
        product.setQuantity(product.getQuantity() - newQuantity);
        productRepository.save(product);

        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price.getPrice());
        cartItem.setTotalPrice(cartItem.getPrice() * quantity);
        userCartRepository.save(cartItem);

        return cartItem;
    }

    public void deleteProduct(int userId, int productId) {
        userCartRepository.findByUserIdAndProductId(userId, productId).ifPresent(cartItem -> {
            userCartRepository.deleteById(cartItem.getId());
            productRepository.findById(productId).ifPresent(product -> {
                product.setQuantity(product.getQuantity() + cartItem.getQuantity());
                productRepository.save(product);
            });
        });
    }

    public double calculateReceipt(int userId) {
        double totalPrice = 0;
        List<CartItem> items = userCartRepository.findAllByUserId(userId);
        List<Deal> deals = dealService.getAllDeals();
        // apply all deals for all items in the cart
        deals.forEach(deal -> {
            log.info("Applying deal={} for userId={}", deal.getDealClassName(), userId);
            dealService.applyDeal(deal, items);
        });
        for(CartItem item: items) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}