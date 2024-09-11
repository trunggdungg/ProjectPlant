package com.example.projectplant.model;

public class Cart {
    int id_tree;
    String name_tree;
    float price_tree;
    String image_tree;
    int quantity;

    public Cart() {
    }

    public int getId_tree() {
        return id_tree;
    }

    public void setId_tree(int id_tree) {
        this.id_tree = id_tree;
    }

    public String getName_tree() {
        return name_tree;
    }

    public void setName_tree(String name_tree) {
        this.name_tree = name_tree;
    }

    public float getPrice_tree() {
        return price_tree;
    }

    public void setPrice_tree(float price_tree) {
        this.price_tree = price_tree;
    }

    public String getImage_tree() {
        return image_tree;
    }

    public void setImage_tree(String image_tree) {
        this.image_tree = image_tree;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
