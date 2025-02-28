package com.example.assignment1;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    private ArrayList<Friend> friendData;
    private ArrayList<Friend> selectedItems = new ArrayList<>();
    private boolean multiSelect = false;
    public CustomAdapter (ArrayList<Friend> data){ //constructor read in data
        this.friendData = data;
    }
    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayoutfriends, parent, false);
        return new CustomAdapter.MyViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
        Friend friend = friendData.get(position);
        holder.name.setText(friend.getName());
        holder.gender.setText(friend.getGender());
        holder.phone.setText("+61 " + friend.getPhone());

        holder.editFriend.setOnClickListener(v -> {
            System.out.println("this is edit click");
            Friend friendInfo = friendData.get(position);

            // Create a Bundle to pass the friend's data
            Bundle bundle = new Bundle();
            bundle.putInt("friendId", friendInfo.getId()); // Assuming 'id' is a unique identifier for the friend
            bundle.putString("name", friendInfo.getName());
            bundle.putString("gender", friendInfo.getGender());
            bundle.putString("phone", friendInfo.getPhone());
            bundle.putString("dob", friendInfo.getDob());
            bundle.putString("hobbies", friend.getHobbies());

            // Load AddFriendFrag with the provided bundle
            AddFriendFrag editFriendFragment = new AddFriendFrag();
            editFriendFragment.setArguments(bundle);
            if (holder.itemView.getContext() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) holder.itemView.getContext();
                // Fragment Transaction
                androidx.fragment.app.FragmentManager fragmentManager = activity.getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.frameLayout2, editFriendFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                // Handle the case where the context is not an AppCompatActivity
                Log.e("CustomAdapter", "Context is not an instance of AppCompatActivity");
            }
        });
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
    private void selectItem(CustomAdapter.MyViewHolder holder, Friend s) {
        if (selectedItems.contains(s)) {
            selectedItems.remove(s);
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            selectedItems.add(s);
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }
    public ArrayList<Friend> getSelectedItems() {
        return selectedItems;
    }

    @Override
    public int getItemCount() {
        return friendData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder  {
        private TextView name,gender,phone;
        private ImageView imageView;
        private Button editFriend;

        public MyViewHolder(View view) { //constructor
            super(view);

            this.name = view.findViewById(R.id.nameTextViewlayout);
            this.gender = view.findViewById(R.id.gendertextViewlayout);
            this.phone = view.findViewById(R.id.phonetextViewlayout);
            this.editFriend = view.findViewById(R.id.editFri);
        }
    }
}
