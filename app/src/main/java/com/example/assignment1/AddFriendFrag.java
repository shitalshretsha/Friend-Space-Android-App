package com.example.assignment1;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriendFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendFrag extends Fragment {
    private EditText dobEditText, hobbiesEditText;
    private TextInputEditText fullNameEditText;
    private TextInputEditText phoneEditText;
    private RadioGroup genderRadioGroup;
    private  Button clearBtn, viewList;
    private ImageView imgback, deletItem, saveButton;
    private int friendId = -1;
    private DatabaseManager dbManager;
    private int year, month, day;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFriendFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFriendFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFriendFrag newInstance(String param1, String param2) {
        AddFriendFrag fragment = new AddFriendFrag();
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
        View addForm = inflater.inflate(R.layout.fragment_add_friend, container, false);

        imgback = addForm.findViewById(R.id.backImageBtn);
        deletItem = addForm.findViewById(R.id.deleteIndividualFriend);
        TextView friendsHeadingText = addForm.findViewById(R.id.friendsHeadingText);
        clearBtn = addForm.findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(e -> clearInputs());
        imgback.setOnClickListener( e -> {
            homeScreenFrag homeFrag = new homeScreenFrag();
            FriendsList listFrag = new FriendsList();
            Bundle bundle;
            bundle = new Bundle();
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Bundle args = getArguments();
            if (args != null) {
                friendId = args.getInt("friendId", -1);
                if (friendId != -1) {
                    listFrag.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frameLayout2, listFrag);

                } else {
                    homeFrag.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frameLayout2, homeFrag);
                }
            }

            fragmentTransaction.commit();
        });
        //declare database and form variable
        dbManager = new DatabaseManager(getActivity());

        fullNameEditText = addForm.findViewById(R.id.textInputEditText1);
        genderRadioGroup = addForm.findViewById(R.id.radioGroup2);
        dobEditText = addForm.findViewById(R.id.dobEditText);
        phoneEditText = addForm.findViewById(R.id.textInputEditPhone);
        hobbiesEditText = addForm.findViewById(R.id.hobiesEditText);
        ImageView saveButton = addForm.findViewById(R.id.saveImgBtn);


        // Disable direct input in the EditText field
        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                String dateString = dobEditText.getText().toString().trim();
                if (!dateString.isEmpty()) {
                    try {
                        // Assuming the date is in the format YYYY-MM-DD
                        String[] parts = dateString.split("-");
                        if (parts.length == 3) {
                            year = Integer.parseInt(parts[0]);
                            month = Integer.parseInt(parts[1]) - 1; // Months are 0-based
                            day = Integer.parseInt(parts[2]);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // Handle parsing error, if necessary
                    }
                }
                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Set the date in the EditText field in the format YYYY-MM-DD
                                dobEditText.setText(selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay);
                            }
                        }, year, month, day);
                // Disable future dates
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(e -> saveFriend());
        Bundle args = getArguments();
        Log.e("CustomAdapter", "asdasd   "+args);

        if (args != null) {
            friendId = args.getInt("friendId", -1);
            if (friendId != -1) {
                // Load friend data for updating
                loadFriendDetails(friendId);
                friendsHeadingText.setVisibility(View.GONE);
                deletItem.setVisibility(View.VISIBLE);
            }else {
                friendsHeadingText.setVisibility(View.VISIBLE);
                deletItem.setVisibility(View.GONE);
            }
        } else {
            friendsHeadingText.setVisibility(View.VISIBLE);
            deletItem.setVisibility(View.GONE);

        }
        deletItem.setOnClickListener(v -> {
            String friend_Id = String.valueOf(friendId);
            dbManager.deleteFriend(friend_Id);

            Toast.makeText(getContext(), "Selected friend deleted", Toast.LENGTH_SHORT).show();

            FriendsList listFrag = new FriendsList();
            Bundle bundle;
            bundle = new Bundle();
            androidx.fragment.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            // create a FragmentTransaction to begin the transaction and replace the Fragment
            // with new Fragment
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout2, listFrag);
            fragmentTransaction.commit();

        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });
        return addForm;
    }

    private void loadFriendDetails(int id) {
        long friendId = getArguments().getLong("friendId", -1);
        String name = getArguments().getString("name");
        String gender = getArguments().getString("gender");
        String phone = getArguments().getString("phone");
        String dob = getArguments().getString("dob");
        String hobbies = getArguments().getString("hobbies");

        // Populate the form with the friend's existing details
        fullNameEditText.setText(name);
        dobEditText.setText(dob);
        phoneEditText.setText(phone);
        hobbiesEditText.setText(hobbies);

        // Set the selected gender
        if (gender.equalsIgnoreCase("Male")) {
            genderRadioGroup.check(R.id.radioButton4);
        } else if (gender.equalsIgnoreCase("Female")) {
            genderRadioGroup.check(R.id.radioButton3);
        }
    }
    private void saveFriend() {
        String fullName = fullNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        int selectedGenderId = genderRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = genderRadioGroup.findViewById(selectedGenderId);
        String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "";
        String dob = dobEditText.getText().toString().trim();
        String hobbies = hobbiesEditText.getText().toString().trim();

        if (fullName.isEmpty() || gender.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean recInserted;
        if (friendId == -1) {
            // New friend, insert into database
            recInserted = dbManager.insertFriend(fullName, gender, phone, dob, hobbies);

            if (recInserted) {
                Toast.makeText(getContext(), "Friend added successfully!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Friend not added!", Toast.LENGTH_SHORT).show();
            }
            // Clear the input fields after adding
            clearInputs();
        } else {
            // Existing friend, update in the database
            dbManager.updateFriend(friendId, fullName, gender, phone, dob, hobbies);
            Toast.makeText(getContext(), "Friend details updated!", Toast.LENGTH_SHORT).show();
        }


    }
    private void clearInputs() {
        fullNameEditText.setText("");
        phoneEditText.setText("");
        genderRadioGroup.clearCheck();
        dobEditText.setText("");
        hobbiesEditText.setText("");
//        avatarEditText.setText("");
    }
}