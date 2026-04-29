package com.example.istream.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "playlist")
public class PlaylistItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String videoUrl;
    private String videoId;

    public PlaylistItem(int userId, String videoUrl, String videoId) {
        this.userId = userId;
        this.videoUrl = videoUrl;
        this.videoId = videoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }
}