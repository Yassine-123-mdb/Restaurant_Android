package com.example.resturants.model;

import java.util.List;

public class Order {
    private List<OrderItem> items;
    private double totalPrice;
    private String timestamp;
    private String status;

    public Order(List<OrderItem> items, double totalPrice, String timestamp, String status) {
        this.items = items;
        this.totalPrice = totalPrice;
        this.timestamp = timestamp;
        this.status = status;
    }
    // Getters et setters

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


