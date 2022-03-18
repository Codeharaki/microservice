package com.course.client.Beans;

import org.springframework.context.annotation.Bean;

import java.util.List;


public class CartBean {
    private Long id;
    private List<CartItemBean> products;

    public CartBean(Long id, List<CartItemBean> products) {
        this.id = id;
        this.products = products;
    }

    public Long getId() {
        return id;
    }
    public void deleteCart()
    {
        this.products.clear();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemBean> getProducts() {
        return products;
    }

    public void setProducts(List<CartItemBean> products) {
        this.products = products;
    }
}