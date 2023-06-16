package com.example.socialgiftprpr.Lists;

import com.example.socialgiftprpr.Lists.Gifts.GiftModel;

import java.util.ArrayList;
import java.util.List;

public class ListModel {

    private int id;
    // Name of the list
    private String name;
    // Description of the list
    private String description;
    // Deadline of the list
    private String deadline;
    private ArrayList<GiftModel> gifts = new ArrayList<>();

    public ListModel(int id, String name, String description, String deadline, ArrayList<GiftModel> gifts){
            this.id = id;
            this.name = name;
            this.description = description;
            this.deadline = deadline;
            this.gifts = gifts;
    }
    // Getters
    public int getId(){
        return id;
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

}
