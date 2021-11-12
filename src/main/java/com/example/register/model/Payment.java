package com.example.register.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private double sum;
    private String account;
    private boolean isExist;
    private String date;

}
