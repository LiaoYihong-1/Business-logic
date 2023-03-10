package com.example.demo.Dao.Device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;
    @Column(nullable = false,name = "type")
    private String type;
    @Column(nullable = false,name = "companyid")
    private Integer company;
    @Column(nullable = false,name="value")
    private Integer value;
    @Column(nullable = false,name="available")
    private Boolean available;
}
