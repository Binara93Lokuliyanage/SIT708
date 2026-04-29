package com.example.istream.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istream.R;
import com.example.istream.database.AppDatabase;
import com.example.istream.database.PlaylistItem;
import com.example.istream.utils.SessionManager;
import com.example.istream.utils.YouTubeUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class HomeActivity extends AppCompatActivity {

    EditText etYoutubeUrl;
    Button btnPlay, btnAddToPlaylist, btnMyPlaylist, btnLogout;
    YouTubePlayerView youtubePlayerView;
    AppDatabase db;
    SessionManager sessionManager;

    private String currentVideoId = null;
    private String currentUrl = null;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = AppDatabase.getInstance(this);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        etYoutubeUrl = findViewById(R.id.etYoutubeUrl);
        btnPlay = findViewById(R.id.btnPlay);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnMyPlaylist = findViewById(R.id.btnMyPlaylist);
        btnLogout = findViewById(R.id.btnLogout);
        youtubePlayerView = findViewById(R.id.youtubePlayerView);

        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer player) {
                youTubePlayer = player;

                String passedUrl = getIntent().getStringExtra("video_url");
                if (passedUrl != null) {
                    etYoutubeUrl.setText(passedUrl);
                    playVideoFromUrl(passedUrl);
                }
            }
        });

        btnPlay.setOnClickListener(v -> {
            String url = etYoutubeUrl.getText().toString().trim();
            playVideoFromUrl(url);
        });

        btnAddToPlaylist.setOnClickListener(v -> addCurrentVideoToPlaylist());

        btnMyPlaylist.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, PlaylistActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void playVideoFromUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            return;
        }

        String videoId = YouTubeUtils.extractVideoId(url);

        if (videoId == null || videoId.length() != 11) {
            Toast.makeText(this, "Please enter a valid YouTube video URL", Toast.LENGTH_SHORT).show();
            return;
        }

        currentVideoId = videoId;
        currentUrl = url;

        Toast.makeText(this, "Loading video...", Toast.LENGTH_SHORT).show();

        if (youTubePlayer != null) {
            youTubePlayer.loadVideo(videoId, 0);
        }
    }

    private void addCurrentVideoToPlaylist() {
        if (currentVideoId == null || currentUrl == null) {
            Toast.makeText(this, "Please play a valid video first", Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        PlaylistItem item = new PlaylistItem(userId, currentUrl, currentVideoId);
        db.playlistDao().insert(item);

        Toast.makeText(this, "Added to playlist", Toast.LENGTH_SHORT).show();
    }
}