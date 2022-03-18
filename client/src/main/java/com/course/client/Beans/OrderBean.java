package com.course.client.Beans;

import java.util.List;

public class OrderBean {
    private Long id;
    private List<OrderItemBean> orders;
    private Double sum;

    public OrderBean(Long id, List<OrderItemBean> orders, Double sum) {
        this.id = id;
        this.orders = orders;
        this.sum = sum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItemBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderItemBean> orders) {
        this.orders = orders;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void total(){
        sum = 0.0;
        for (OrderItemBean order: orders){
            sum += order.getPrice()*order.getQuantity();
        }};
    public void addItem(OrderItemBean order) {
        this.orders.add(order);
        this.sum += order.getPrice()* order.getQuantity();
    }
}
