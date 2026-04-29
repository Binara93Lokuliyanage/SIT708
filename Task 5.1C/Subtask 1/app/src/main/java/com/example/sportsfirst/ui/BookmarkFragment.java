package com.example.sportsfirst.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsfirst.R;
import com.example.sportsfirst.adapters.NewsAdapter;
import com.example.sportsfirst.models.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookmarkFragment extends Fragment {

    private RecyclerView recyclerView;
    private final List<NewsItem> bookmarkList = new ArrayList<>();

    public BookmarkFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        recyclerView = view.findViewById(R.id.rvBookmarks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadBookmarks();

        recyclerView.setAdapter(new NewsAdapter(bookmarkList));

        return view;
    }

    private void loadBookmarks() {
        bookmarkList.clear();

        SharedPreferences prefs = requireContext()
                .getSharedPreferences("bookmarks", Context.MODE_PRIVATE);

        Map<String, ?> all = prefs.getAll();

        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String title = entry.getKey();
            String[] parts = entry.getValue().toString().split("\\|");

            if (parts.length >= 4) {
                String desc = parts[0];
                int img = Integer.parseInt(parts[1]);
                String category = parts[2];
                String type = parts[3];

                bookmarkList.add(new NewsItem(title, desc, img, category, type));
            }
        }
    }
}