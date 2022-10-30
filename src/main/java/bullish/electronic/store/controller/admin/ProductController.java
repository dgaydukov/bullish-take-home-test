package bullish.electronic.store.controller.admin;

import bullish.electronic.store.model.entity.Product;
import bullish.electronic.store.model.entity.ProductPrice;
import bullish.electronic.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("product")
    public Product createProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }
    @DeleteMapping("product/{productId}")
    public void deleteProduct(@PathVariable int productId){
        productService.deleteProduct(productId);
    }

    @GetMapping("product/{productId}")
    public Product getProduct(@PathVariable int productId){
        return productService.getProduct(productId).orElse(null);
    }

    @GetMapping("product")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    // we don't remove prices, but always add new based with start/end -dates
    @PostMapping("product/{productId}/price")
    public ProductPrice addNewPrice(@PathVariable int productId, @RequestBody ProductPrice productPrice){
        productPrice.setProductId(productId);
        return productService.savePrice(productPrice);
    }
    @GetMapping("product/{productId}/price/")
    public List<ProductPrice> getAllPrices(@PathVariable int productId){
        return productService.getAllPrices(productId);
    }
}
