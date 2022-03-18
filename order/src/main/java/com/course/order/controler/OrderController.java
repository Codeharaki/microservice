package com.course.order.controler;

import com.course.order.entity.Orders;
import com.course.order.entity.OrdersItem;
import com.course.order.repositories.OrderItemRepository;
import com.course.order.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@RestController
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderItemRepository orderItemRepository;
    // Create orders
    @CrossOrigin
    @PostMapping(value = "/order")
    public ResponseEntity<Orders> createNewOrder()
    {
        Orders order = orderRepository.save(new Orders());
        if (order == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Couldn't create a new order");
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    // Get order by Id
    @GetMapping(value = "/order/{id}")
    public Optional<Orders> getOrder(@PathVariable Long id){
        Optional<Orders> order = orderRepository.findById(id);
        if (order == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't get order");
        return order;
    }

    // Add order
    @PostMapping(value = "/order/{id}")
    @Transactional
    public ResponseEntity<OrdersItem> addOrderItemToOrder(@PathVariable Long id, @RequestBody OrdersItem ordersItem){
        Orders commande = orderRepository.getOne(id);
        if (commande == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Couldn't get order");
        commande.addOrderItem(ordersItem);
        orderRepository.save(commande);
        return new ResponseEntity<>(ordersItem, HttpStatus.CREATED);
    }
}
