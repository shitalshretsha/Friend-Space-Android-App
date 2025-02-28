package com.example.assignment1;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link homeScreenFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class homeScreenFrag extends Fragment {

    private CardView cardAddFriend, cardViewList, cardEvents;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public homeScreenFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment homeScreenFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static homeScreenFrag newInstance(String param1, String param2) {
        homeScreenFrag fragment = new homeScreenFrag();
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
        View addForm = inflater.inflate(R.layout.fragment_home_screen, container, false);

//        ImageView img = addForm.findViewById(R.id.addNewIcon);
        cardAddFriend = addForm.findViewById(R.id.card1);
        cardAddFriend.setOnClickListener( e -> {
            AddFriendFrag fragment2 = new AddFriendFrag();
            Bundle bundle;
            bundle = new Bundle();
            fragment2.setArguments(bundle);
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, fragment2);
            fragmentTransaction.commit();
        });
//        ImageView list = addForm.findViewById(R.id.viewListIcon);
        cardViewList = addForm.findViewById(R.id.card2);
        cardViewList.setOnClickListener( e -> {
            FriendsList fragment = new FriendsList();
            Bundle bundle;
            bundle = new Bundle();
            fragment.setArguments(bundle);
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, fragment);
            fragmentTransaction.commit();
        });

        cardAddFriend = addForm.findViewById(R.id.card3);
        cardAddFriend.setOnClickListener( e -> {
            EventsFarg fragment = new EventsFarg();
            Bundle bundle;
            bundle = new Bundle();
            fragment.setArguments(bundle);
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, fragment);
            fragmentTransaction.commit();
        });

        return addForm;
    }
}