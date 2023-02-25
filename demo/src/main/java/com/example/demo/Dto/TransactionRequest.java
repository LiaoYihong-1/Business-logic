package com.example.demo.Dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class TransactionRequest {
    private Integer device;
    private Integer company;
    private Integer customer;
    private Integer amount;
}
