package bullish.electronic.store.service;

import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.repository.ProductPriceRepository;
import bullish.electronic.store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductPriceRepository priceRepository;

    public Product saveProduct(Product product){
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    public Optional<Product> getProduct(int id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void deleteProduct(int productId){
        productRepository.deleteById(productId);
    }

    public ProductPrice savePrice(ProductPrice productPrice){
        return priceRepository.save(productPrice);
    }
    public List<ProductPrice> getAllPrices(int productId){
        return priceRepository.findAllByProductId(productId);
    }
}