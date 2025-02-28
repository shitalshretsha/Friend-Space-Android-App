package com.example.assignment1;

import java.util.Objects;

public class Friend {
    private int id;
    private String name;
    private String phone;
    private String gender;
    private String dob;
    private String hobbies;


    // Constructor
    public Friend(int id, String name, String gender, String phone,  String dob, String hobbies) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
        this.hobbies = hobbies;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getDob() {
        return dob;
    }
    public String getHobbies() {
        return hobbies;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return id == friend.id; // Assuming 'id' is the unique identifier
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}