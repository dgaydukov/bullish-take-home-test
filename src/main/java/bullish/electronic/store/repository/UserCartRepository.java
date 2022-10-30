package bullish.electronic.store.repository;

import bullish.electronic.store.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCartRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByUserIdAndProductId(int userId, int productId);

    List<CartItem> findAllByUserId(int userId);
}
