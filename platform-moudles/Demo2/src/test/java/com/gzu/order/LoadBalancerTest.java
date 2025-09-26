package com.gzu.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

@SpringBootTest
public class LoadBalancerTest {
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Test
    void test(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("test");
        System.out.println("choose = "+ serviceInstance.getHost()+":"+serviceInstance.getPort());
        serviceInstance = loadBalancerClient.choose("test");
        System.out.println("choose = "+ serviceInstance.getHost()+":"+serviceInstance.getPort());
        serviceInstance = loadBalancerClient.choose("test");
        System.out.println("choose = "+ serviceInstance.getHost()+":"+serviceInstance.getPort());
        serviceInstance = loadBalancerClient.choose("test");
        System.out.println("choose = "+ serviceInstance.getHost()+":"+serviceInstance.getPort());
        serviceInstance = loadBalancerClient.choose("test");
        System.out.println("choose = "+ serviceInstance.getHost()+":"+serviceInstance.getPort());

    }

}
