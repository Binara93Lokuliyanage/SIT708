package com.example.sportsfirst.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsfirst.R;
import com.example.sportsfirst.adapters.FeaturedAdapter;
import com.example.sportsfirst.adapters.NewsAdapter;
import com.example.sportsfirst.models.NewsData;
import com.example.sportsfirst.models.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private RecyclerView rvFeatured, rvNews;
    private EditText searchBar;
    private Button btnBookmarks, btnFullList;
    private TextView tvSectionTitle;

    private final List<NewsItem> allNews = new ArrayList<>();
    private final List<NewsItem> featuredList = new ArrayList<>();
    private final List<NewsItem> latestList = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvFeatured = view.findViewById(R.id.rvFeatured);
        rvNews = view.findViewById(R.id.rvNews);
        searchBar = view.findViewById(R.id.searchBar);
        btnBookmarks = view.findViewById(R.id.btnBookmarks);
        btnFullList = view.findViewById(R.id.btnFullList);
        tvSectionTitle = view.findViewById(R.id.tvSectionTitle);

        loadNewsData();
        setupRecyclerViews();
        showLatestNews();

        searchBar.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            boolean isSearchAction = actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE;

            boolean isEnterKey = event != null
                    && event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;

            if (isSearchAction || isEnterKey) {
                String query = searchBar.getText().toString().trim();
                filterLatestNewsByCategory(query);
                return true;
            }

            return false;
        });

        btnBookmarks.setOnClickListener(v -> showBookmarks());

        btnFullList.setOnClickListener(v -> {
            searchBar.setText("");
            showLatestNews();
        });

        return view;
    }

    private void loadNewsData() {
        allNews.clear();
        featuredList.clear();
        latestList.clear();

        allNews.addAll(NewsData.getAllNews());

        for (NewsItem item : allNews) {
            if ("featured".equalsIgnoreCase(item.getType())) {
                featuredList.add(item);
            } else if ("latest".equalsIgnoreCase(item.getType())) {
                latestList.add(item);
            }
        }
    }

    private void setupRecyclerViews() {
        rvFeatured.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvNews.setLayoutManager(
                new LinearLayoutManager(getContext()));

        rvFeatured.setAdapter(new FeaturedAdapter(featuredList));
    }

    private void showLatestNews() {
        tvSectionTitle.setText("Latest News");
        rvNews.setAdapter(new NewsAdapter(new ArrayList<>(latestList)));
    }

    private void showBookmarks() {
        List<NewsItem> bookmarkList = loadBookmarks();
        tvSectionTitle.setText("Bookmarks");
        rvNews.setAdapter(new NewsAdapter(bookmarkList));
    }

    private void filterLatestNewsByCategory(String query) {
        List<NewsItem> filteredList = new ArrayList<>();

        if (query.isEmpty()) {
            showLatestNews();
            return;
        }

        for (NewsItem item : latestList) {
            if (item.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        tvSectionTitle.setText(query);
        rvNews.setAdapter(new NewsAdapter(filteredList));
    }

    private List<NewsItem> loadBookmarks() {
        List<NewsItem> bookmarkList = new ArrayList<>();

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

        return bookmarkList;
    }
}