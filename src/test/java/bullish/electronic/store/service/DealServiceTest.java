package bullish.electronic.store.service;

import bullish.electronic.store.model.entity.CartItem;
import bullish.electronic.store.model.entity.Deal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DealServiceTest {
    @Autowired
    private DealService dealService;

    @Test
    @Order(2)
    public void saveAndGetDealTest(){
        Deal newDeal = getNewDeal();

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
    @Order(1)
    public void getAllAndDeleteDealTest(){
        System.out.println(dealService.getAllDeals());
        Assert.isTrue(dealService.getAllDeals().size() == 0, "No deals should be there");

        Deal savedDeal1 = dealService.saveDeal(getNewDeal());
        Deal savedDeal2 = dealService.saveDeal(getNewDeal());

        Assert.isTrue(dealService.getAllDeals().size() == 2, "2 deals should be there");

        dealService.deleteDeal(savedDeal1.getId());
        Assert.isTrue(dealService.getAllDeals().size() == 1, "1 deal should be there");
    }

    @Test
    @Order(3)
    public void applyDealTest(){
        Deal deal = dealService.saveDeal(getNewDeal());
        List<CartItem> items = new ArrayList<>();
        dealService.applyDeal(deal, items);
    }

    @Test
    @Order(4)
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
}