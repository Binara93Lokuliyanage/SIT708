package com.example.planmate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planmate.data.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> events;

    private OnDeleteClickListener deleteListener;
    private OnEditClickListener editListener;

    public interface OnDeleteClickListener {
        void onDelete(Event event);
    }

    public interface OnEditClickListener {
        void onEdit(Event event);
    }

    public EventAdapter(OnDeleteClickListener deleteListener, OnEditClickListener editListener) {
        this.deleteListener = deleteListener;
        this.editListener = editListener;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, category, location, timeText;
        Button updateBtn, deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleText);
            category = itemView.findViewById(R.id.categoryText);
            location = itemView.findViewById(R.id.locationText);
            timeText = itemView.findViewById(R.id.timeText);
            updateBtn = itemView.findViewById(R.id.updateBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (events == null) return;

        Event event = events.get(position);

        holder.title.setText(event.title);
        holder.category.setText(event.category);
        holder.location.setText(event.location);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        holder.timeText.setText(sdf.format(new Date(event.dateTime)));

        if (editListener != null) {
            holder.updateBtn.setOnClickListener(v -> editListener.onEdit(event));
        }

        if (deleteListener != null) {
            holder.deleteBtn.setOnClickListener(v -> deleteListener.onDelete(event));
        }
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }
}