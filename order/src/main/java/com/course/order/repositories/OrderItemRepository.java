package com.course.order.repositories;

import com.course.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Orders, Long> {
}
