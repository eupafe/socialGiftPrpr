package com.example.socialgiftprpr.Lists.Gifts;

import android.os.Parcelable;

import java.io.Serializable;

public class GiftModel implements Serializable{

    private int giftId;
    private int wishlistId;
    // Link of the list
    private String productUrl;
    private int priority;
    // If the gift is saved or not
    private boolean save;

    public GiftModel(int giftId, int wishlistId, String productUrl, int priority, Boolean save){
        this.giftId = giftId;
        this.wishlistId = wishlistId;
        this.productUrl = productUrl;
        this.priority = priority;
        this.save = save;
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

    // Setters
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }
    public void setSave(Boolean save){
        this.save = save;
    }

    @Override
    public String toString() {
        return "GiftModel{" +
                "giftId=" + giftId +
                ", wishlistId=" + wishlistId +
                ", productUrl='" + productUrl + '\'' +
                ", priority=" + priority +
                ", save=" + save +
                '}';
    }
}
