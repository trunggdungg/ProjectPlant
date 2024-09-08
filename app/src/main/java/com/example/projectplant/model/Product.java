package com.example.projectplant.model;

import com.esotericsoftware.kryo.Serializer;

import java.io.Serializable;

public class Product implements Serializable {
    private int id_tree;
    private int id_category;
    private String image_tree;
    private String name_tree,color_tree;
    private float price_tree;
    private String  info_tree;

    public int getId_category() {
        return id_category;
    }

    public void setId_category(int id_category) {
        this.id_category = id_category;
    }

    public float getPrice_tree() {
        return price_tree;
    }

    public void setPrice_tree(float price_tree) {
        this.price_tree = price_tree;
    }

    public int getId_tree() {
        return id_tree;
    }

    public void setId_tree(int id_tree) {
        this.id_tree = id_tree;
    }

    public Product(int id_tree, String info_tree, float price_tree, String color_tree, String name_tree, int id_category, String image_tree) {
        this.id_tree = id_tree;
        this.info_tree = info_tree;
        this.price_tree = price_tree;
        this.color_tree = color_tree;
        this.name_tree = name_tree;
        this.id_category = id_category;
        this.image_tree = image_tree;
    }

    public String getImage_tree() {
        return image_tree;
    }

    public void setImage_tree(String image_tree) {
        this.image_tree = image_tree;
    }

    public String getName_tree() {
        return name_tree;
    }

    public void setName_tree(String name_tree) {
        this.name_tree = name_tree;
    }

    public String getColor_tree() {
        return color_tree;
    }

    public void setColor_tree(String color_tree) {
        this.color_tree = color_tree;
    }

    public String getInfo_tree() {
        return info_tree;
    }

    public void setInfo_tree(String info_tree) {
        this.info_tree = info_tree;
    }
}
