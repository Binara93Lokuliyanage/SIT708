package com.example.lostfound.database;

public class Advert {
    public int id;
    public String type;
    public String name;
    public String phone;
    public String description;
    public String date;
    public String location;
    public String category;
    public String imageUri;
    public String createdAt;

    public Advert(int id, String type, String name, String phone, String description,
                  String date, String location, String category, String imageUri, String createdAt) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.imageUri = imageUri;
        this.createdAt = createdAt;
    }
}