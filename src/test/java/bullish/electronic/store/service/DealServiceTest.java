package bullish.electronic.store.service;

import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.model.entity.Deal;
import bullish.electronic.store.repository.DealRepository;
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
class DealServiceTest {
    @Mock
    private DealRepository dealRepository;
    @InjectMocks
    private DealService dealService;

    @Test
    public void saveAndGetDealTest(){
        Deal newDeal = getNewDeal();
        Mockito.when(dealRepository.save(newDeal)).thenReturn(getMockDeal());
        Mockito.when(dealRepository.findById(getMockDeal().getId())).thenReturn(Optional.of(getMockDeal()));

        Deal savedDeal = dealService.saveDeal(newDeal);
        Assert.isTrue(savedDeal.getId() > 0, "dealId must be greater then 0");
        Assert.isTrue(savedDeal.getName().equals(newDeal.getName()), "deal name should be the same");
        Assert.isTrue(savedDeal.getDealClassName().equals(newDeal.getDealClassName()), "deal class name should be the same");

        Optional<Deal> optDeal1 = dealService.getDeal(savedDeal.getId());
        Assert.isTrue(optDeal1.isPresent(), "Deal with dealId=" + savedDeal.getId() + " is missing");
        Deal deal1 = optDeal1.get();
        Assert.isTrue(deal1.getName().equals(newDeal.getName()), "deal name should be the same");
        Assert.isTrue(deal1.getDealClassName().equals(newDeal.getDealClassName()), "deal class name should be the same");

        final int missingDealId = 9999;
        Optional<Deal> optDeal2 = dealService.getDeal(missingDealId);
        Assert.isTrue(optDeal2.isEmpty(), "Deal with dealId=" + missingDealId + " shouldn't exist");
    }

    @Test
    public void getAllAndDeleteDealTest(){
        Assert.isTrue(dealService.getAllDeals().size() == 0, "No deals should be there");
        Deal mockDeal = getNewDeal();
        mockDeal.setId(1);
        Mockito.when(dealRepository.save(Mockito.any())).thenReturn(mockDeal);

        Deal savedDeal1 = dealService.saveDeal(getNewDeal());
        Deal savedDeal2 = dealService.saveDeal(getNewDeal());

        Mockito.when(dealRepository.findAll()).thenReturn(new ArrayList<>(List.of(savedDeal1, savedDeal2)));
        Assert.isTrue(dealService.getAllDeals().size() == 2, "2 deals should be there");

        Mockito.when(dealRepository.findAll()).thenReturn(new ArrayList<>(List.of(savedDeal1)));
        dealService.deleteDeal(savedDeal1.getId());
        Assert.isTrue(dealService.getAllDeals().size() == 1, "1 deal should be there");
    }

    @Test
    public void applyDealTest(){
        Mockito.when(dealRepository.save(Mockito.any())).thenReturn(getMockDeal());
        Deal deal = dealService.saveDeal(getNewDeal());
        List<CartItem> items = new ArrayList<>();
        dealService.applyDeal(deal, items);
    }

    @Test
    public void applyWrongDealTest(){
        Deal deal = getNewDeal();
        deal.setDealClassName("badDeal");
        dealService.saveDeal(deal);
        List<CartItem> items = new ArrayList<>();
        RuntimeException ex1 = Assertions.assertThrows(RuntimeException.class, () -> dealService.applyDeal(deal, items));
        Assertions.assertEquals(ex1.getMessage(), "Failed to load ProductDeal for className=bullish.electronic.store.model.deals.badDealProductDeal");
    }

    public static Deal getNewDeal(){
        Deal deal = new Deal();
        deal.setName("Get 50% off for second item");
        deal.setDealClassName("Get50PercentOffForSecondItem");
        return deal;
    }
    public static Deal getMockDeal(){
        Deal deal = getNewDeal();
        deal.setId(1);
        return deal;
    }
}