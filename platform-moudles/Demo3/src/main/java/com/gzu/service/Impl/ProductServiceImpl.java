package com.gzu.service.Impl;


import com.gzu.prouct.entity.Product;
import com.gzu.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();
        product.setProductId(productId);
        product.setProductName("Product Name"+product);
        product.setNum(2);
        product.setPrice(BigDecimal.valueOf(100));



        return product;
    };
}
