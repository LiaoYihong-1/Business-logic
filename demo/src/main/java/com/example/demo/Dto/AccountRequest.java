package com.example.demo.Dto;

import lombok.Data;

@Data
public class AccountRequest {
    String type;
    String name;
    String password;
}
