package the.convenient.foodie.discount.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public class ScoreDto implements Serializable {

    @NotNull(message = "User ID should not be null")
    private String user_id;

    @NotNull(message = "Number of orders should not be null")
    @Positive(message = "Number of orders can not be negative")
    private Integer number_of_orders;

    @NotNull(message = "Money spent should not be null")
    @Positive(message = "Money spent can not be negative")
    private Integer money_spent;

    public ScoreDto() {
        this.number_of_orders = 0;
        this.money_spent = 0;
    }

    public ScoreDto(String user_id, Integer number_of_orders, Integer money_spent) {
        this.user_id = user_id;
        this.number_of_orders = number_of_orders;
        this.money_spent = money_spent;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getNumber_of_orders() {
        return number_of_orders;
    }

    public void setNumber_of_orders(Integer number_of_orders) {
        this.number_of_orders = number_of_orders;
    }

    public Integer getMoney_spent() {
        return money_spent;
    }

    public void setMoney_spent(Integer money_spent) {
        this.money_spent = money_spent;
    }
}
