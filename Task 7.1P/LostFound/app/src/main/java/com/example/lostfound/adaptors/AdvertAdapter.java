package com.example.lostfound.adaptors;

import android.net.Uri;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostfound.R;
import com.example.lostfound.database.Advert;

import java.util.ArrayList;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder> {

    ArrayList<Advert> advertList;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Advert advert);
    }

    public AdvertAdapter(ArrayList<Advert> advertList, OnItemClickListener listener) {
        this.advertList = advertList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdvertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advert_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertAdapter.ViewHolder holder, int position) {
        Advert advert = advertList.get(position);

        holder.tvTitle.setText(advert.type + ": " + advert.name);
        holder.tvCategory.setText("Category: " + advert.category);
        holder.tvLocation.setText("Location: " + advert.location);
        holder.tvDate.setText("Posted: " + advert.createdAt);

        holder.image.setImageURI(Uri.parse(advert.imageUri));

        holder.itemView.setOnClickListener(v -> listener.onItemClick(advert));
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvTitle, tvCategory, tvLocation, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}