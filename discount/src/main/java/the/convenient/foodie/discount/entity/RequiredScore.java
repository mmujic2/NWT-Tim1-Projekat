package the.convenient.foodie.discount.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "requiredscore")
public class RequiredScore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer     id;
    @Column(name = "orders_required", columnDefinition = "integer")
    private Integer     orders_required;
    @Column(name = "money_required", columnDefinition = "integer")
    private Integer     money_required;

    public RequiredScore(Integer orders_required, Integer money_required) {
        this.orders_required = orders_required;
        this.money_required = money_required;
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
