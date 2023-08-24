package edu.itmo.blps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response<T> {
    Boolean success;
    String description;
    T extra;
}
