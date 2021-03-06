package com.course.client.Proxies;

import com.course.client.Beans.OrderBean;
import com.course.client.Beans.OrderItemBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "ms-order", url = "localhost:8093")
public interface MsOrderProxy {
    @PostMapping(value ="/order")
    public ResponseEntity<OrderBean> createNewOrder();
    @GetMapping(value="/order/{id}")
    public Optional<OrderBean> getOrder(@PathVariable Long id);
    @PostMapping(value = "/order/{id}")
    public ResponseEntity<OrderBean> addOrderItemToOrder (@PathVariable Long id, @RequestBody OrderItemBean orderItem);
}
