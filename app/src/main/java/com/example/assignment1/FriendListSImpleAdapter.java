package com.example.assignment1;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendListSImpleAdapter extends RecyclerView.Adapter<FriendListSImpleAdapter.MyViewHolder>{
    private ArrayList<Friend> friendData;
    private ArrayList<Friend> selectedItems = new ArrayList<>();
    private boolean multiSelect = false;
    public FriendListSImpleAdapter (ArrayList<Friend> data){ //constructor read in data
        this.friendData = data;
    }
    @NonNull
    @Override
    public FriendListSImpleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_event_layout, parent, false);
        return new FriendListSImpleAdapter.MyViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendListSImpleAdapter.MyViewHolder holder, int position) {
        Friend friend = friendData.get(position);
        holder.name1.setText(friend.getName());
        holder.gender1.setText(friend.getGender());
        holder.phone1.setText("+61 " + friend.getPhone());
        preSelectItem(holder, friend);
        holder.itemView.setOnClickListener(v -> {
            if (multiSelect) {
                selectItem(holder, friend);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            multiSelect = true;
            selectItem(holder, friend);
            return true;
        });
    }
    public void selectItem(MyViewHolder holder, Friend s) {
        if (selectedItems.contains(s)) {
            selectedItems.remove(s);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            selectedItems.add(s);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }
    public void preSelectItem(MyViewHolder holder, Friend s) {
        if (selectedItems.contains(s)) {
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    public void setSelectedItems(ArrayList<Friend> selectedFriends) {
        this.selectedItems.clear();
        this.selectedItems.addAll(selectedFriends);
        notifyDataSetChanged(); // Update the views
    }
    public ArrayList<Friend> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public int getItemCount() {
           return friendData.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView name1,gender1,phone1;
        private ImageView imageView;
        private Button editFriend;

        public MyViewHolder(View view) { //constructor
            super(view);

            this.name1 = view.findViewById(R.id.name);
            this.gender1 = view.findViewById(R.id.gender);
            this.phone1 = view.findViewById(R.id.phoneNo);
        }
    }
}
