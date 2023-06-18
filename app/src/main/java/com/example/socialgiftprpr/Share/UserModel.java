package com.example.socialgiftprpr.Share;

public class UserModel {

    // User id
    private String id;
    // User name
    private String name;
    // User surname
    private String surname;
    // User email
    private String email;
    // User image link
    private String image;

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

    public String getImage() {
        return image;
    }
}
