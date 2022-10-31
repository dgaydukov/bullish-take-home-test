package bullish.electronic.store.service;

import bullish.electronic.store.exception.NotEnoughQuantityException;
import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.repository.ProductPriceRepository;
import bullish.electronic.store.repository.ProductRepository;
import bullish.electronic.store.repository.UserCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class UserCartServiceTest {
    @Mock
    private UserCartRepository userCartRepository;
    @Mock
    private ProductPriceRepository productPriceRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private DealService dealService;

    @InjectMocks
    private UserCartService userCartService;

    private static final int USER_ID = 10;

    @Test
    void addProductTest() {
        final int QUANTITY = 5;
        final Product product = ProductServiceTest.getNewProduct();
        product.setId(1);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productPriceRepository.findOneByProductIdOrderByIdDesc(product.getId())).thenReturn(Optional.of(new ProductPrice()));

        CartItem item = userCartService.addProduct(USER_ID, product.getId(), QUANTITY);
        Assert.isTrue(item.getUserId() == USER_ID, "userId not matching");
        Assert.isTrue(item.getProductId() == product.getId(), "productId not matching");
        Assert.isTrue(item.getQuantity() == QUANTITY, "quantity not matching");

        NotEnoughQuantityException ex = Assertions.assertThrows(NotEnoughQuantityException.class, () -> userCartService.addProduct(USER_ID, product.getId(), 11));
        Assertions.assertEquals(ex.getMessage(), "Not enough products, requestedQuantity=11, availableQuantity=5, for productId=1");
    }

    @Test
    void deleteProductTest() {
        userCartService.deleteProduct(USER_ID, 1);
    }

    @Test
    void calculateReceiptTest() {
        Assert.isTrue(userCartService.calculateReceipt(USER_ID) == 0, "receipt should be 0");
        List<CartItem> items = new ArrayList<>();
        items.add(getCartItem());
        items.add(getCartItem());
        Mockito.when(userCartRepository.findAllByUserId(USER_ID)).thenReturn(items);
        Assert.isTrue(userCartService.calculateReceipt(USER_ID) == 400, "receipt should be 400");
    }

    public static CartItem getCartItem(){
        CartItem item = new CartItem();
        item.setProductId(1);
        item.setUserId(USER_ID);
        item.setQuantity(2);
        item.setPrice(100);
        item.setTotalPrice(200);
        return item;
    }
}