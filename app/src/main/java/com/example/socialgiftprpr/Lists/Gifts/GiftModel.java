package com.example.socialgiftprpr.Lists.Gifts;

import java.io.Serializable;

public class GiftModel implements Serializable{

    // Variables
    private int giftId;
    private int wishlistId;
    private String productUrl;
    private int priority;
    private boolean save;
    private ProductModel productInfo;

    public GiftModel(int giftId, int wishlistId, String productUrl, int priority, Boolean save, ProductModel productInfo){
        this.giftId = giftId;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = priority;
        this.save = save;
        this.productInfo = productInfo;
    }

    // Getters
    public int getGiftId(){
        return giftId;
    }
    public int getWishlistId(){
        return wishlistId;
    }
    public String getProductUrl() {
        return productUrl;
    }
    public int getPriority(){
        return priority;
    }
    public boolean getSave(){
        return save;
    }
    public ProductModel getProductInfo() {
        return productInfo;
    }

    // Setters
    public void setProductInfo(ProductModel productModel) {
        this.productInfo = productModel;
    }

    @Override
    public String toString() {
        return "GiftModel{" +
                "giftId=" + giftId +
                ", wishlistId=" + wishlistId +
                ", productUrl='" + productUrl + '\'' +
                ", priority=" + priority +
                ", save=" + save +
                ", productInfo=" + productInfo +
                '}';
    }
}
