package com.gzu.feign;

import com.gzu.feign.fallback.ProductFeignClientFallback;
import com.gzu.prouct.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(value = "test",fallback = ProductFeignClientFallback.class)
public interface ProductFeignClient {
    //mvc的两套使用逻辑
    //1、标注在Controller上，是接受这样的请求
    //2、标注在FeignClient上，是发送这样的请求
    @GetMapping("product/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
