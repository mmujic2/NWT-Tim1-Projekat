package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;
    @Column(name = "user_id", unique = true ,columnDefinition = "integer")
    private Integer user_id;
    @Column(name = "number_of_orders", columnDefinition = "integer")
    private Integer number_of_orders;
    @Column(name = "money_spent", columnDefinition = "integer")
    private Integer money_spent;

    public Score(Integer user_id, Integer number_of_orders, Integer money_spent) {
        this.user_id = user_id;
        this.number_of_orders = number_of_orders;
        this.money_spent = money_spent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
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
