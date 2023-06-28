package com.example.socialgiftprpr.Lists;

import com.example.socialgiftprpr.Lists.Gifts.GiftModel;

import java.util.ArrayList;

public class ListModel {

    // Variables
    private int id;
    private int userId;
    private String creationDate;
    private String name;
    private String description;
    private String deadline;
    private ArrayList<GiftModel> gifts = new ArrayList<>();

    public ListModel(int id, int userId, String name, String description, String creationDate, String deadline, ArrayList<GiftModel> gifts){
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.description = description;
            this.creationDate = creationDate;
            this.deadline = deadline;
            this.gifts = gifts;
    }

    // Getters
    public int getId(){
        return id;
    }
    public int getUserId(){
        return userId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getName(){
            return name;
        }
    public String getDescription() {
        return description;
    }
    public String getDeadline() {
        return deadline;
    }

    public ArrayList<GiftModel> getGifts(){
        return gifts;
    }

    // Setters
    public void setName(String name){
            this.name = name;
        }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "ListModel{" +
                "id=" + id +
                ", userId=" + userId +
                ", creationDate='" + creationDate + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", gifts=" + gifts +
                '}';
    }
}
