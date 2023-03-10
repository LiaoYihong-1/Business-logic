package com.example.demo.Dao.Cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @Column(nullable = false,name="customerid")
    private Integer customer;
    @Column(nullable = false,name="deviceid")
    private Integer device;
}
