package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import the.convenient.foodie.discount.dto.ScoreDto;

@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;

    @NotNull(message = "User ID should not be null")
    @Column(name = "user_uuid", unique = true)
    private String user_uuid;

    @NotNull(message = "Number of orders should not be null")
    @PositiveOrZero(message = "Number of orders can not be negative")
    @Column(name = "number_of_orders", columnDefinition = "integer")
    private Integer number_of_orders;

    @NotNull(message = "Money spent should not be null")
    @PositiveOrZero(message = "Money spent can not be negative")
    @Column(name = "money_spent", columnDefinition = "integer")
    private Integer money_spent;

    public Score() {
        this.user_uuid = null;
        this.number_of_orders = 0;
        this.money_spent = 0;
    }

    public Score(String user_id, Integer number_of_orders, Integer money_spent) {
        this.user_uuid = user_id;
        this.number_of_orders = number_of_orders;
        this.money_spent = money_spent;
    }

    public Score(ScoreDto scoreDto) {
        this.user_uuid = scoreDto.getUser_id();
        this.number_of_orders = scoreDto.getNumber_of_orders();
        this.money_spent = scoreDto.getMoney_spent();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_uuid;
    }

    public void setUser_id(String user_id) {
        this.user_uuid = user_id;
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
