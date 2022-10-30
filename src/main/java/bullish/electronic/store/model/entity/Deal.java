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
 * For simplicity we assume that all deals applied to all products
 */
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long startDate;
    private long endDate;
    private String name;
    private String desc;
    private String dealClassName;
}
