package com.example.socialgiftprpr.Lists.Gifts;

import java.io.Serializable;

public class ProductModel implements Serializable {

    // Variables
    private int id;
    private String name;
    private String desctiption;
    private String link;
    private String photo;
    private double price;

    public ProductModel(int id, String name, String description, String link, String photo, double price){
        this.id = id;
        this.name = name;
        this.desctiption = description;
        this.link = link;
        this.photo = photo;
        this.price = price;

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return desctiption;
    }

    public String getLink() {
        return link;
    }

    public String getPhoto() {
        return photo;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desctiption='" + desctiption + '\'' +
                ", link='" + link + '\'' +
                ", photo='" + photo + '\'' +
                ", price=" + price +
                '}';
    }
}
