package com.example.sportsfirst.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsfirst.R;
import com.example.sportsfirst.adapters.NewsAdapter;
import com.example.sportsfirst.models.NewsData;
import com.example.sportsfirst.models.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private static final String ARG_NEWS = "news_item";
    private NewsItem newsItem;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(NewsItem item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_NEWS, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            newsItem = (NewsItem) getArguments().getSerializable(ARG_NEWS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if (newsItem == null) {
            return view;
        }

        ImageView image = view.findViewById(R.id.detailImage);
        TextView title = view.findViewById(R.id.detailTitle);
        TextView desc = view.findViewById(R.id.detailDescription);
        Button btnBookmark = view.findViewById(R.id.btnBookmark);
        RecyclerView rvRelated = view.findViewById(R.id.rvRelated);

        image.setImageResource(newsItem.getImageResId());
        title.setText(newsItem.getTitle());
        desc.setText(newsItem.getDescription());

        btnBookmark.setOnClickListener(v -> {
            saveBookmark(newsItem);
            Toast.makeText(getContext(), "Bookmarked!", Toast.LENGTH_SHORT).show();
        });

        rvRelated.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRelated.setAdapter(new NewsAdapter(getRelatedNews()));

        return view;
    }

    private List<NewsItem> getRelatedNews() {
        List<NewsItem> allNews = NewsData.getAllNews();
        List<NewsItem> relatedList = new ArrayList<>();

        for (NewsItem item : allNews) {
            boolean sameCategory = item.getCategory().equalsIgnoreCase(newsItem.getCategory());
            boolean notSameTitle = !item.getTitle().equalsIgnoreCase(newsItem.getTitle());

            if (sameCategory && notSameTitle) {
                relatedList.add(item);
            }
        }

        return relatedList;
    }

    private void saveBookmark(NewsItem item) {
        SharedPreferences prefs = requireContext()
                .getSharedPreferences("bookmarks", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();

        String value = item.getDescription() + "|" + item.getImageResId() + "|" + item.getCategory() + "|" + item.getType();
        editor.putString(item.getTitle(), value);
        editor.apply();
    }
}