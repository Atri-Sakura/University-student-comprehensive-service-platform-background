package com.gzu.controller;


import com.gzu.prouct.entity.Product;
import com.gzu.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RequestMapping("/api/product")
@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long  productId, HttpServletRequest request) {
        Product product = productService.getProductById(productId);
        String header = request.getHeader("X-Token");
        System.out.println("hello......token=【"+header+"】");
        return product;
    }
}
