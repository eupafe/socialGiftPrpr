package com.example.socialgiftprpr.Lists.Gifts;

import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

public class GiftModel implements Serializable{

    private int giftId;
    private int wishlistId;
    // Link of the list
    private String productUrl;
    private int priority;
    // If the gift is saved or not
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
                ", productInfo=" + productInfo +
                '}';
    }

    public void setProductInfo(ProductModel productModel) {
        this.productInfo = productModel;
    }
}
