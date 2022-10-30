package bullish.electronic.store.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
/**
 * Storing prices in separate table has 2 advantages:
 * 1. you can have time discount (just set new price for only 6 hours)
 * 2. you have all price change history
 */
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    int productId;
    double price;
    long startDate;
    long endDate;
}
