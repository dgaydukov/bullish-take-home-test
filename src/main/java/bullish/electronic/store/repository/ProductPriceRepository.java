package bullish.electronic.store.repository;

import bullish.electronic.store.model.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
    List<ProductPrice> findAllByProductId(int productId);

    Optional<ProductPrice> findOneByProductIdOrderByIdDesc(int productId);
}
