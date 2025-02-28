package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(toolbar);
        homeScreenFrag fragment1 = new homeScreenFrag();
        loadFragment(fragment1);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection.

        int id = item.getItemId();
        if (id == R.id.optionHome){

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.friendLismenu){
            loadFragment(new FriendsList());
            return true;
        } else if (id == R.id.eventMenu){

            loadFragment(new AddEventFrag());
            return true;
        } else if (id == R.id.EventListmenu){

            loadFragment(new EventsFarg());
            return true;
        } else if (id == R.id.addFriendMenu){

            loadFragment(new AddFriendFrag());
            return true;
        }
        else return super.onOptionsItemSelected(item);

    }
    public void loadFragment(Fragment fragment){
        //create an instance of FragmentManager
        androidx.fragment.app.FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        // with new Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}