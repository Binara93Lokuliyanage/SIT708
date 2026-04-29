package com.example.sportsfirst.models;

import java.io.Serializable;

public class NewsItem implements Serializable {

    private String title;
    private String description;
    private int imageResId;
    private String category;
    private String type;

    public NewsItem(String title, String description, int imageResId, String category, String type) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.category = category;
        this.type = type;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getCategory() { return category; }
    public String getType() { return type; }
}