package bullish.electronic.store.service;

import bullish.electronic.store.model.deals.ProductDeal;
import bullish.electronic.store.model.entity.Deal;
import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;

    public Deal saveDeal(Deal deal){
        Deal savedDeal = dealRepository.save(deal);
        return savedDeal;
    }

    public Optional<Deal> getDeal(int dealId){
        return dealRepository.findById(dealId);
    }

    public List<Deal> getAllDeals(){
        return dealRepository.findAll();
    }

    public void deleteDeal(int dealId){
        dealRepository.deleteById(dealId);
    }

    public void applyDeal(Deal deal, List<CartItem> items){
        // load plugin class
        final String fullClassName = "bullish.electronic.store.model.deals." + deal.getDealClassName() + "ProductDeal";
        try {
            ProductDeal productDeal = (ProductDeal) Class.forName(fullClassName).
                    getDeclaredConstructor().newInstance();
            productDeal.applyDeal(items);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex){
            throw new RuntimeException("Failed to load ProductDeal for className=" + fullClassName, ex);
        } catch (Exception ex){
            throw new RuntimeException("Failed to apply deal for deal=" + deal.getDealClassName(), ex);
        }
    }
}