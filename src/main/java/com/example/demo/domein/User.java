package com.example.demo.domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity 
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() { return username;
    }
    public void setName(String name) {
        this.username = name;
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", name=" + username + "]";
    }
}

