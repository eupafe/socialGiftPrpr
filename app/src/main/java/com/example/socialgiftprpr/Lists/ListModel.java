package com.example.socialgiftprpr.Lists;

public class ListModel {

    private int id;
    // Name of the list
    private String name;
    // Description of the list
    private String description;
    // Deadline of the list
    private String deadline;
    // If the task is saved or not
    private boolean save;

    public ListModel(int id, String name, String description, String deadline, boolean save){
            this.id = id;
            this.name = name;
            this.description = description;
            this.deadline = deadline;
            this.save = save;
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
    public boolean getSave(){
            return save;
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
    public void setSave(Boolean save){
            this.save = save;
        }

    @Override
    public String toString() {
        return "ListModel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", save=" + save +
                '}';
    }
}
