package com.example.socialgiftprpr.Lists.Gifts;

public class GiftModel {

    // Name of the gift
    private String name;
    // Link of the list
    private String link;

    private String priority;
    // If the gift is saved or not
    private boolean save;

    public GiftModel(String name,String priority,  String link,  boolean save){
        this.name = name;
        this.priority = priority;
        this.link = link;
        this.save = save;
    }
    // Getters
    public String getName(){
        return name;
    }
    public String getLink() {
        return link;
    }
    public boolean getSave(){
        return save;
    }

    // Setters
    public void setName(String name){
        this.name = name;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public void setSave(Boolean save){
        this.save = save;
    }

}
