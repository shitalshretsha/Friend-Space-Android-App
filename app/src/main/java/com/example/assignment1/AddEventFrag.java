package com.example.assignment1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEventFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEventFrag extends Fragment {

    private EditText etDateTime, etEventName, etEventLocation;
    private Calendar calendar;
    private DatabaseManager mydManger;
    private RecyclerView friendRec;
    private ImageView backBtn, deletItem;
    private ArrayList<Friend> friendList;
    private FriendListSImpleAdapter adapter;
    private boolean isEditing = false;
    private int currentEventId = -1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int eventId = -1;
    public AddEventFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddEventFrag newInstance(String param1, String param2) {
        AddEventFrag fragment = new AddEventFrag();
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
        View view = inflater.inflate(R.layout.fragment_add_events, container, false);
        mydManger = new DatabaseManager(getActivity());
        mydManger.openReadable();
        // Load or hardcode participants
        friendList = fetchFriendsFromDB();
        etEventName = view.findViewById(R.id.eventNameEdit);
        etEventLocation = view.findViewById(R.id.evenLocationEdit);
        deletItem = view.findViewById(R.id.deleteIndividualEvent);
        backBtn = view.findViewById(R.id.eventBackBtn);
        TextView eventHeadingText = view.findViewById(R.id.eventHeadingText);
        Button cleareventform = view.findViewById(R.id.cleareventform);
        cleareventform.setOnClickListener(e -> clearInputs());
        backBtn.setOnClickListener( e -> {
            EventsFarg eventList = new EventsFarg();
            Bundle bundle;
            bundle = new Bundle();
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, eventList);

            fragmentTransaction.commit();
        });
        // Initialize RecyclerView
        friendRec = view.findViewById(R.id.frinedListInEvent);
        friendRec.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FriendListSImpleAdapter(friendList);
        friendRec.setAdapter(adapter);
        friendRec.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        etDateTime = view.findViewById(R.id.dateTimeEdit);
        calendar = Calendar.getInstance();

        // Open Date and Time Picker when EditText is clicked
        etDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimePicker();
            }
        });

        view.findViewById(R.id.saveImgBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrUpdateEvent();
            }
        });

        Bundle args = getArguments();
        Log.e("CustomAdapter", "asdasd   "+args);

        if (args != null) {
            eventId = args.getInt("eventId", -1);
            if (eventId != -1) {
                // Load event data for updating
                loadEventDetails(eventId);
                backBtn.setVisibility(View.VISIBLE);
                eventHeadingText.setVisibility(View.GONE);
                deletItem.setVisibility(View.VISIBLE);
            }  else {
                deletItem.setVisibility(View.GONE);

            }
        } else {
            deletItem.setVisibility(View.GONE);
            backBtn.setVisibility(View.GONE);
            eventHeadingText.setVisibility(View.VISIBLE);
        }

        deletItem.setOnClickListener(v -> {
            String event_Id = String.valueOf(eventId);
            mydManger.deleteEventById(event_Id);

            Toast.makeText(getContext(), "Selected events deleted", Toast.LENGTH_SHORT).show();

            EventsFarg eventList = new EventsFarg();
            Bundle bundle;
            bundle = new Bundle();
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, eventList);
            fragmentTransaction.commit();

        });
        return view;
    }
    private void loadEventDetails(int id) {
//        int eventId = getArguments().getInt("eventId", -1);
        String name = getArguments().getString("name");
        String location = getArguments().getString("location");
        String date = getArguments().getString("date");

        // Populate the form with the event's existing details
        etEventName.setText(name);
        etEventLocation.setText(location);
        etDateTime.setText(date);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            calendar.setTime(sdf.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (eventId == -1) {
            // New event, so clear selected participants
//            adapter.setSelectedItems(new ArrayList<>()); // Clear all selected items
        }
        ArrayList<Friend> selectedFriends = mydManger.fetchFriendsForEvent(eventId);
        adapter.setSelectedItems(selectedFriends);// Implement this method

    }

    //source: youtube tutorial and stackoverflow https://www.digitalocean.com/community/tutorials/android-date-time-picker-dialog https://stackoverflow.com/questions/47071734/how-retrieve-the-value-from-datepickerdialog
    private void showDateTimePicker() {
        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set the date selected in the calendar object
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // After setting the date, show TimePickerDialog
                        showTimePicker();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
//        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
    //source: youtube tutorial and stackoverflow https://www.digitalocean.com/community/tutorials/android-date-time-picker-dialog
    private void showTimePicker() {
        // Show TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set the time selected in the calendar object
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        // Format the selected date and time
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                        String formattedDateTime = sdf.format(calendar.getTime());

                        // Set the formatted date and time in the EditText
                        etDateTime.setText(formattedDateTime);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);  // Use true for 24-hour format, false for 12-hour format
        timePickerDialog.show();
    }

    private void saveOrUpdateEvent() {
        String name = etEventName.getText().toString();
        String location = etEventLocation.getText().toString();
        String dateTime = etDateTime.getText().toString();
        if (name.isEmpty() || location.isEmpty() || dateTime.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get selected friend IDs from adapter
        ArrayList<Friend> selectedFriendIds = adapter.getSelectedItems();

        if (eventId != -1) {
            // Update existing event
            mydManger.updateEvent(eventId, name, location, dateTime);
            mydManger.updateEventFriends(eventId, selectedFriendIds);
            Toast.makeText(getContext(), "Event updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            // Insert new event
            long neweventId = mydManger.insertEvent(name, location, dateTime);

            if (neweventId != -1) {
                mydManger.addFriendsToEvent((int) neweventId, selectedFriendIds);
            }
            Toast.makeText(getContext(), "Events added successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
        }
    }
    private ArrayList<Friend> fetchFriendsFromDB() {
        ArrayList<Friend> friends = new ArrayList<>();
        Cursor cursor = mydManger.fetchAllFriends();

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

    private void clearInputs() {
        etEventName.setText("");
        etEventLocation.setText("");
        etDateTime.setText("");
//        adapter.setSelectedItems(new ArrayList<>());
        adapter.getSelectedItems().clear();
        adapter.notifyDataSetChanged();

    }

}