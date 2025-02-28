package com.example.assignment1;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsList extends Fragment {
    private DatabaseManager mydManger;
    private ImageView deleteBtn, searchBtn;
    private TextView noItemsFoundText;
    private RecyclerView friendRec;
    private ArrayList<Friend> friendList;
    private CustomAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsList.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsList newInstance(String param1, String param2) {
        FriendsList fragment = new FriendsList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View manageView = inflater.inflate(R.layout.fragment_friends_list, container, false);
        mydManger = new DatabaseManager(getActivity());
        mydManger.openReadable();

        friendRec = manageView.findViewById(R.id.friendRec);
        friendRec.setLayoutManager(new LinearLayoutManager(getActivity()));
        friendList = fetchFriendsFromDB("");
        adapter = new CustomAdapter(friendList);
        friendRec.setAdapter(adapter);

        EditText searchInput = manageView.findViewById(R.id.searchInput);
        searchBtn = manageView.findViewById(R.id.searchBtn);
        deleteBtn = manageView.findViewById(R.id.deleteFriend);
        noItemsFoundText = manageView.findViewById(R.id.noItemsFound);
        searchBtn.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            ArrayList<Friend> results = fetchFriendsFromDB(query);
            if (results.isEmpty()) {
                noItemsFoundText.setVisibility(View.VISIBLE); // Show "No items found" message
                friendRec.setVisibility(View.GONE); // Hide RecyclerView
            } else {
                noItemsFoundText.setVisibility(View.GONE); // Hide "No items found" message
                friendRec.setVisibility(View.VISIBLE); // Show RecyclerView
                friendList.clear();
                friendList.addAll(results);
                adapter.notifyDataSetChanged();
            }
        });

        deleteBtn.setOnClickListener(v -> {
            ArrayList<Friend> selectedItems = adapter.getSelectedItems();
            if (selectedItems.size() > 0) {
                // Delete friend from the database
                for (Friend friend : selectedItems) {
                    String friend_Id = String.valueOf(friend.getId());
                    mydManger.deleteFriend(friend_Id);
                }
                // Remove selected items from the list
                friendList.removeAll(selectedItems);

                // Notify adapter of changes
                adapter.notifyDataSetChanged();

                // Clear selected items
                adapter.getSelectedItems().clear();

                Toast.makeText(getContext(), "Selected item deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "No item selected", Toast.LENGTH_SHORT).show();
            }
        });
        loadFriends("");
        return manageView;
    }
    private void loadFriends(String query) {
        ArrayList<Friend> results = fetchFriendsFromDB(query);
        if (results.isEmpty()) {
            noItemsFoundText.setVisibility(View.VISIBLE); // Show "No items found" message
            friendRec.setVisibility(View.GONE); // Hide RecyclerView
        } else {
            noItemsFoundText.setVisibility(View.GONE); // Hide "No items found" message
            friendRec.setVisibility(View.VISIBLE); // Show RecyclerView
            friendList.clear();
            friendList.addAll(results);
            adapter.notifyDataSetChanged();
        }
    }
    private ArrayList<Friend> fetchFriendsFromDB(String query) {
        ArrayList<Friend> friends = new ArrayList<>();
        Cursor cursor = mydManger.searchFriendsByName(query);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String dob = cursor.getString(cursor.getColumnIndexOrThrow("dob"));
                String hobbies = cursor.getString(cursor.getColumnIndexOrThrow("hobbies"));

                friends.add(new Friend(id, name, gender, phone, dob, hobbies));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return friends;
    }
}