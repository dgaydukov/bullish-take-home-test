package bullish.electronic.store.controller.admin;

import bullish.electronic.store.model.entity.Deal;
import bullish.electronic.store.service.DealService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class DealController {
    private final DealService dealService;

    @PostMapping("deal")
    public Deal createDeal(@RequestBody Deal deal){
        return dealService.saveDeal(deal);
    }
    @DeleteMapping("deal/{dealId}")
    public void deleteDeal(@PathVariable int dealId){
        dealService.deleteDeal(dealId);
    }

    @GetMapping("deal/{dealId}")
    public Deal getDeal(@PathVariable int dealId){
        return dealService.getDeal(dealId).orElse(null);
    }

    @GetMapping("deal")
    public List<Deal> getAllDeals(){
        return dealService.getAllDeals();
    }
}
