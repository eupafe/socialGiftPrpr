package com.example.socialgiftprpr.Share;

public class UserModel {

    // Name of the user
    private String name;
    // Surname of the user
    private String surname;
    // Total number of lists
    private int numLists;
    // Total number of gifts
    private int numGifts;
    // Total number of friends
    private int numFriends;

    public UserModel(String name){
        this.name = name;
    }

    public UserModel(String name, String surname, int numLists, int numGifts, int numFriends){
        this.name = name;
        this.surname = surname;
        this.numLists = numLists;
        this.numGifts = numGifts;
        this.numFriends = numFriends;
    }

    public String getName() {
        return name;
    }
}
