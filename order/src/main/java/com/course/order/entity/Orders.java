package com.course.order.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Orders {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrdersItem> orderList;
    private Double sum=0.0;

    public Orders(Long id, List<OrdersItem> orderList, Double sum) {
        this.id = id;
        this.orderList = orderList;
        this.sum = sum;
    }

    public Orders() {
    }

    public void totalsOrder(){
        sum = 0.0;
        for (OrdersItem order: orderList){
            sum += order.getPrice()*order.getQuantity();
        }
    }

    public Long getIdOrder() {
        return id;
    }

    public void setIdOrder(Long idOrder) {
        this.id = idOrder;
    }

    public List<OrdersItem> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrdersItem> orderList) {
        this.orderList = orderList;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public void addOrderItem (OrdersItem ordersItem){
        this.orderList.add(ordersItem);
        this.sum += ordersItem.getPrice()* ordersItem.getQuantity();
    }
}
