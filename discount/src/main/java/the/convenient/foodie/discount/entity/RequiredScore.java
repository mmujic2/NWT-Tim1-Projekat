package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import the.convenient.foodie.discount.dto.RequiredScoreDto;

@Entity
@Table(name = "requiredscore")
public class RequiredScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;

    @NotNull(message = "Orders required should not be null")
    @Positive(message = "Orders required can not be negative")
    @Column(name = "orders_required", columnDefinition = "integer")
    private Integer     orders_required;

    @NotNull(message = "Money required should not be null")
    @Positive(message = "Money required can not be negative")
    @Column(name = "money_required", columnDefinition = "integer")
    private Integer     money_required;


    public RequiredScore() {
        this.orders_required = 0;
        this.money_required = 0;
    }

    public RequiredScore(Integer orders_required, Integer money_required) {
        this.orders_required = orders_required;
        this.money_required = money_required;
    }

    public RequiredScore(RequiredScoreDto requiredScoreDto) {
        this.orders_required = requiredScoreDto.getOrders_required();
        this.money_required = requiredScoreDto.getMoney_required();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrders_required() {
        return orders_required;
    }

    public void setOrders_required(Integer orders_required) {
        this.orders_required = orders_required;
    }

    public Integer getMoney_required() {
        return money_required;
    }

    public void setMoney_required(Integer money_required) {
        this.money_required = money_required;
    }
}
