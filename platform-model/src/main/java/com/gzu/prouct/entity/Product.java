package com.gzu.prouct.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long productId;
    private String productName;
    private int num;
    private BigDecimal price;
}
