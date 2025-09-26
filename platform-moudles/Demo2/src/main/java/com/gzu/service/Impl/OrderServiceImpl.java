package com.gzu.service.Impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gzu.feign.ProductFeignClient;
import com.gzu.order.entity.Order;
import com.gzu.prouct.entity.Product;
import com.gzu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;

    @SentinelResource(value = "createOrder",blockHandler = "createOrderFallBack")
    @Override
    public Order createOrder(Long userId, Long productId) {
//        Product product = getProductFromRemoteBalancer(productId);
        //使用feign完成请求调用 自动负载均衡轮询
        Product product = productFeignClient.getProductById(productId);
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setAddress("nihao");
        order.setNickName("zhangsan");
        order.setProductList(Arrays.asList(product));

        return order;

    }
    public Order createOrderFallBack(Long userId, Long productId, BlockException e) {

        Order order1 = new Order();
        order1.setUserId(0l);
        order1.setTotalAmount(BigDecimal.ZERO);
        order1.setAddress("error"+e.getMessage());
        order1.setNickName("0");
        System.out.println(e.getMessage());

        return order1;
    }

    private Product getProductFromRemoteWithLoadBalance(Long productId){
        ServiceInstance choose = loadBalancerClient.choose("test");
        String url = "http://"+ choose.getHost() + ":" + choose.getPort()+"/product/"+productId;
        log.info("远程登陆连接{}",url);
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
    private Product getProductFromRemote(Long productId){
        List<ServiceInstance> instances = discoveryClient.getInstances("test");
        ServiceInstance serviceInstance = instances.get(0);
        String url = "http://"+ serviceInstance.getHost() + ":" + serviceInstance.getPort()+"/product/"+productId;
        log.info("远程登陆连接{}",url);
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
    private Product getProductFromRemoteBalancer(Long productId){

        String url = "http://test/product/"+productId;
        log.info("远程登陆连接{}",url);
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
}
