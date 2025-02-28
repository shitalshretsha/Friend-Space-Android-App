package com.example.assignment1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class eventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Event> eventList; // Store event names in a simple ArrayList
    private static final int VIEW_TYPE_PAST = 0;
    private static final int VIEW_TYPE_FUTURE = 1;
    private ArrayList<Event> selectedItems = new ArrayList<>(); // Track selected events
    private Context context;

    public eventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_PAST) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pasteven_list_layout, parent, false);
            return new PastEventViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.futureevent_list_layout, parent, false);
            return new FutureEventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Event event = eventList.get(position);
        if (holder instanceof PastEventViewHolder) {
            ((PastEventViewHolder) holder).bind(event);
        } else if (holder instanceof FutureEventViewHolder) {
            ((FutureEventViewHolder) holder).bind(event);
        }

        // Handle item clicks for selection
        holder.itemView.setOnClickListener(v -> {
            Event eventInfo = eventList.get(position);

            // Create a Bundle to pass the friend's data
            Bundle bundle = new Bundle();
            bundle.putInt("eventId", eventInfo.getId()); // Assuming 'id' is a unique identifier for the friend
            bundle.putString("name", eventInfo.getName());
            bundle.putString("location", eventInfo.getLocation());
            bundle.putString("date", eventInfo.getDate());

            // Load AddFriendFrag with the provided bundle
            AddEventFrag editEvent = new AddEventFrag();
            editEvent.setArguments(bundle);
            if (holder.itemView.getContext() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
                // Perform the Fragment Transaction
                androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout2, editEvent)
                        .addToBackStack(null)
                        .commit();
            } else {
                // Handle the case where the context is not an AppCompatActivity
                Log.e("CustomAdapter", "Context is not an instance of AppCompatActivity");
            }
        });

    }

    // Select/deselect item and update background color
    private void selectItem(RecyclerView.ViewHolder holder, Event event) {
        if (selectedItems.contains(event)) {
            selectedItems.remove(event);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            selectedItems.add(event);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    // Get selected items for deletion
    public ArrayList<Event> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
    public int getItemViewType(int position) {
        Event event = eventList.get(position);
        // You might need to determine the view type based on some property of Event
        return event.isPast() ? VIEW_TYPE_PAST : VIEW_TYPE_FUTURE;
    }
    public class PastEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, locationTextView, dateTextView;

        public PastEventViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextViewlayout);
            locationTextView = itemView.findViewById(R.id.locationtextViewlayout);
            dateTextView = itemView.findViewById(R.id.datetextViewlayout);
        }

        public void bind(Event event) {
            eventNameTextView.setText(event.getName());
            locationTextView.setText(event.getLocation());
            dateTextView.setText(event.getDate());
        }
    }

    public class FutureEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventNameTextView, locationTextView, dateTextView;

        public FutureEventViewHolder(View itemView) {
            super(itemView);
            eventNameTextView = itemView.findViewById(R.id.eventFNameTextlayout);
            locationTextView = itemView.findViewById(R.id.locationFtextViewlayout);
            dateTextView = itemView.findViewById(R.id.dateFtextViewlayout);
        }

        public void bind(Event event) {
            eventNameTextView.setText(event.getName());
            locationTextView.setText(event.getLocation());
            dateTextView.setText(event.getDate());
        }
    }
}
