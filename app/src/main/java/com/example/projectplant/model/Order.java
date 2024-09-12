package com.example.projectplant.model;

public class Order {
    private int id_user;
    private String status;
    private float price;
    private String address_shipping;
    private long timestamp;

    public Order() {
    }

    public Order(int id_user, String address_shipping, float price, String status) {
        this.id_user = id_user;
        this.address_shipping = address_shipping;
        this.price = price;
        this.status = status;
    }

    public Order(int id_user, String status, float price, String address_shipping, long timestamp) {
        this.id_user = id_user;
        this.status = status;
        this.price = price;
        this.address_shipping = address_shipping;
        this.timestamp = timestamp;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAddress_shipping() {
        return address_shipping;
    }

    public void setAddress_shipping(String address_shipping) {
        this.address_shipping = address_shipping;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
