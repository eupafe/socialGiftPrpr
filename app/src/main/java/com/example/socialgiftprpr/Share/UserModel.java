package com.example.socialgiftprpr.Share;

public class UserModel {

    private String id;
    // Name of the user
    private String name;
    // Surname of the user
    private String surname;
    private String email;
    private String image;

    // Total number of lists
    private int numLists;
    // Total number of gifts
    private int numGifts;
    // Total number of friends
    private int numFriends;

    public UserModel(String name){
        this.name = name;
    }

    public UserModel(String id, String name, String surname, String email, String image){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.image = image;

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }
}
