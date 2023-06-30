package com.example.socialgiftprpr.Share;

public class UserModel {

    // Variables
    private String id;
    private String name;
    private String surname;
    private String email;
    private String image;

    public UserModel(String id, String name, String surname, String email, String image){
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.image = image;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSurname() {
        return surname;
    }
    public String getEmail() {
        return email;
    }
}
