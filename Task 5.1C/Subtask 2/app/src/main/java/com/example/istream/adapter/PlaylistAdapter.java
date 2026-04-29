package com.example.istream.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istream.R;
import com.example.istream.database.PlaylistItem;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(PlaylistItem item);
    }

    private final List<PlaylistItem> playlist;
    private final OnItemClickListener listener;

    public PlaylistAdapter(List<PlaylistItem> playlist, OnItemClickListener listener) {
        this.playlist = playlist;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem item = playlist.get(position);
        holder.tvVideoUrl.setText(item.getVideoUrl());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView tvVideoUrl;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVideoUrl = itemView.findViewById(R.id.tvVideoUrl);
        }
    }
}