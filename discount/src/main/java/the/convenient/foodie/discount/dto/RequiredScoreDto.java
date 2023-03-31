package the.convenient.foodie.discount.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public class RequiredScoreDto implements Serializable {

    @NotNull(message = "Orders required should not be null")
    @Positive(message = "Orders required can not be negative")
    private Integer     orders_required;

    @NotNull(message = "Money required should not be null")
    @Positive(message = "Money required can not be negative")
    private Integer     money_required;

    public RequiredScoreDto() {
        this.orders_required = 0;
        this.money_required = 0;
    }

    public RequiredScoreDto(Integer orders_required, Integer money_required) {
        this.orders_required = orders_required;
        this.money_required = money_required;
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
