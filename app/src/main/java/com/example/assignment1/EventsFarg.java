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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFarg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFarg extends Fragment {

    private DatabaseManager mydManger;
    private TextView displayMessage;
    private RecyclerView pastRec, futureRec;
//    private ArrayList<Friend> friendList;
    private eventAdapter adapter;
    private ArrayList<Event> eventList, futureEvents;
    private Button deleteButton;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsFarg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFarg.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFarg newInstance(String param1, String param2) {
        EventsFarg fragment = new EventsFarg();
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
        View manageView = inflater.inflate(R.layout.fragment_events_farg, container, false);
        mydManger = new DatabaseManager(getActivity());
        mydManger.openReadable();

        //past event list view
        pastRec = manageView.findViewById(R.id.pastEventRecycler);
        eventList = fetchPastEvents();
        pastRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new eventAdapter(getContext(), eventList);
        pastRec.setAdapter(adapter);

        //future events list view
        futureRec = manageView.findViewById(R.id.futureEventRecycler);
        futureEvents = fetchFutureEvents();
        futureRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new eventAdapter(getContext(), futureEvents);
        futureRec.setAdapter(adapter);
        return manageView;
    }

    private ArrayList<Event> fetchPastEvents() {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = mydManger.fetchPastEvents(); // Define logic to fetch past events
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("event_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("event_location"));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("event_date_time"));
                events.add(new Event(id, name, location, dateTime, true));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    private ArrayList<Event> fetchFutureEvents() {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = mydManger.fetchFutureEvents(); // Define logic to fetch future events
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("event_id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("event_location"));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow("event_date_time"));
                events.add(new Event(id, name, location, dateTime, false));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }
}