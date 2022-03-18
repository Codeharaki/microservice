package com.example.demo.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cart {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> products;

    public Cart() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItem> getProducts() {
        return products;
    }

    public void setProducts(List<CartItem> products) {
        this.products = products;
    }

    public Cart(Long id, List<CartItem> products) {
        this.id = id;
        this.products = products;
    }

    public void addProduct(CartItem cartItem) {
        this.products.add(cartItem);
    }

    public void deleteProducts() {
        this.products.removeAll(products);
    }
    public void deleteProduct(int id) {
        this.products.remove(products.get(id));
    }
}
