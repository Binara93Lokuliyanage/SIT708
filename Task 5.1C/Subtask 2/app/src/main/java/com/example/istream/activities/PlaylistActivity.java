package com.example.istream.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istream.R;
import com.example.istream.adapter.PlaylistAdapter;
import com.example.istream.database.AppDatabase;
import com.example.istream.database.PlaylistItem;
import com.example.istream.utils.SessionManager;

import java.util.List;

public class PlaylistActivity extends AppCompatActivity {

    RecyclerView recyclerPlaylist;
    Button btnBackHome, btnLogoutPlaylist;
    AppDatabase db;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        recyclerPlaylist = findViewById(R.id.recyclerPlaylist);
        btnBackHome = findViewById(R.id.btnBackHome);
        btnLogoutPlaylist = findViewById(R.id.btnLogoutPlaylist);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        int userId = sessionManager.getUserId();
        List<PlaylistItem> playlist = db.playlistDao().getPlaylistByUserId(userId);

        recyclerPlaylist.setLayoutManager(new LinearLayoutManager(this));
        recyclerPlaylist.setAdapter(new PlaylistAdapter(playlist, item -> {
            Intent intent = new Intent(PlaylistActivity.this, HomeActivity.class);
            intent.putExtra("video_url", item.getVideoUrl());
            startActivity(intent);
        }));

        btnBackHome.setOnClickListener(v -> finish());

        btnLogoutPlaylist.setOnClickListener(v -> {
            sessionManager.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}