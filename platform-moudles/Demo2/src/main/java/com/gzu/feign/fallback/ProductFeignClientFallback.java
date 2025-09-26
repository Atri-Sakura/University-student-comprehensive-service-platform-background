package com.gzu.feign.fallback;

import com.gzu.feign.ProductFeignClient;
import com.gzu.prouct.entity.Product;

public class ProductFeignClientFallback implements ProductFeignClient {

    @Override
    public Product getProductById(Long id) {
        System.out.println("兜底回调");
        return new Product();
    }
}
