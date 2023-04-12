package com.example.demo;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class FoodieEvent {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Column(name = "timestamp")
    String timestamp;

    @Column(name = "action")
    String action;

    @Column(name = "event")
    String event;

    @Column(name = "service_name")
    String serviceName;

    @Column(name = "user")
    String userid = "22";

    public FoodieEvent(String timestamp, String action, String event, String serviceName, String userid) {
        this.timestamp = timestamp;
        this.action = action;
        this.event = event;
        this.serviceName = serviceName;
        this.userid = userid;
    }

    public FoodieEvent() {

    }
}
