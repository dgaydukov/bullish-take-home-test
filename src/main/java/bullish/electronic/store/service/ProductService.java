package bullish.electronic.store.service;

import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.repository.ProductPriceRepository;
import bullish.electronic.store.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository priceRepository;

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