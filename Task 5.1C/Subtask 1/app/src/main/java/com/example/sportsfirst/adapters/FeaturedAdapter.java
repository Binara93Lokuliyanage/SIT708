package com.example.sportsfirst.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsfirst.ui.DetailFragment;
import com.example.sportsfirst.R;
import com.example.sportsfirst.models.NewsItem;

import java.util.List;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder> {

    private List<NewsItem> featuredList;

    public FeaturedAdapter(List<NewsItem> featuredList) {
        this.featuredList = featuredList;
    }

    @NonNull
    @Override
    public FeaturedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured, parent, false);

        return new FeaturedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedViewHolder holder, int position) {

        NewsItem item = featuredList.get(position);

        holder.title.setText(item.getTitle());
        holder.image.setImageResource(item.getImageResId());

        holder.itemView.setOnClickListener(v -> {
            DetailFragment fragment = DetailFragment.newInstance(item);

            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return featuredList.size();
    }

    public static class FeaturedViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        public FeaturedViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.featuredImage);
            title = itemView.findViewById(R.id.featuredTitle);
        }
    }
}