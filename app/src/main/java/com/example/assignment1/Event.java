package com.example.assignment1;

public class Event {
    private int id;
    private String name;
    private String location;
    private String date;
    private boolean isPast;

    public Event(int id, String name, String location, String date, boolean isPast) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.isPast = isPast;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPast() {
        return isPast;
    }

    public void setPast(boolean past) {
        isPast = past;
    }
}
