package bullish.electronic.store.service;

import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.repository.ProductPriceRepository;
import bullish.electronic.store.repository.ProductRepository;
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
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductPriceRepository productPriceRepository;

    @InjectMocks
    private ProductService productService;



    @Test
    public void saveAndGetProductTest(){
        Product newProduct = getNewProduct();
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(getMockProduct());
        Mockito.when(productRepository.findById(getMockProduct().getId())).thenReturn(Optional.of(getMockProduct()));

        Product savedProduct = productService.saveProduct(newProduct);
        Assert.isTrue(savedProduct.getId() > 0, "productId must be greater then 0");
        Assert.isTrue(savedProduct.getName().equals(newProduct.getName()), "product name should be the same");
        Assert.isTrue(savedProduct.getWeight() == newProduct.getWeight(), "product weight should be the same");
        Assert.isTrue(savedProduct.getQuantity() == newProduct.getQuantity(), "product quantity should be the same");

        Optional<Product> optProduct1 = productService.getProduct(savedProduct.getId());
        Assert.isTrue(optProduct1.isPresent(), "Product with productId=" + savedProduct.getId() + " is missing");
        Product product1 = optProduct1.get();
        Assert.isTrue(savedProduct.getName().equals(product1.getName()), "product name should be the same");
        Assert.isTrue(savedProduct.getWeight() == product1.getWeight(), "product weight should be the same");
        Assert.isTrue(savedProduct.getQuantity() == product1.getQuantity(), "product quantity should be the same");

        final int missingProductId = 9999;
        Optional<Product> optProduct2 = productService.getProduct(missingProductId);
        Assert.isTrue(optProduct2.isEmpty(), "Product with productId=" + missingProductId + " shouldn't exist");
    }

    @Test
    public void getAllAndDeleteProductTest(){
        Assert.isTrue(productService.getAllProducts().size() == 0, "No products should be there");
        Mockito.when(productRepository.save(Mockito.any())).thenReturn(getMockProduct());

        Product savedProduct1 = productService.saveProduct(getNewProduct());
        Product savedProduct2 = productService.saveProduct(getNewProduct());


        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>(List.of(savedProduct1, savedProduct2)));
        Assert.isTrue(productService.getAllProducts().size() == 2, "2 products should be there");

        productService.deleteProduct(savedProduct1.getId());
        Mockito.when(productRepository.findAll()).thenReturn(new ArrayList<>(List.of(savedProduct1)));
        Assert.isTrue(productService.getAllProducts().size() == 1, "1 products should be there");
    }

    @Test
    public void setAndGetPrices(){
        final int productId = 1;
        ProductPrice pp = new ProductPrice();
        pp.setProductId(productId);
        pp.setPrice(49.99);
        Assert.isTrue(productService.getAllPrices(productId).size() == 0, "No product price should be there");
        ProductPrice mockProductPrice = new ProductPrice();
        mockProductPrice.setProductId(pp.getProductId());
        mockProductPrice.setPrice(pp.getPrice());
        mockProductPrice.setId(1);
        Mockito.when(productPriceRepository.save(Mockito.any())).thenReturn(mockProductPrice);
        ProductPrice savedPP = productService.savePrice(pp);

        Assert.isTrue(savedPP.getId() > 0, "productPriceId should be greater then 0");
        Assert.isTrue(pp.getProductId() == savedPP.getProductId(), "productId should match");
        Assert.isTrue(pp.getPrice() == savedPP.getPrice(), "prices should match");

        List<ProductPrice> mockProductPriceList = new ArrayList<>();
        mockProductPriceList.add(mockProductPrice);
        Mockito.when(productPriceRepository.findAllByProductId(productId)).thenReturn(mockProductPriceList);
        Assert.isTrue(productService.getAllPrices(productId).size() == 1, "1 product price should be there");
    }

    public static Product getNewProduct(){
        Product product = new Product();
        product.setName("spoon");
        product.setWeight(100);
        product.setQuantity(10);
        return product;
    }
    public static Product getMockProduct(){
        Product product = getNewProduct();
        product.setId(1);
        return product;
    }
}