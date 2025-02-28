package com.example.assignment1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseManager {
    private final Context context;
    private SQLHelper helper;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "FriendSpaceDB.db";
    private static final int DATABASE_VERSION = 3;
    private static final String Friend_TABLE_NAME = "friends";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FULL_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DOB = "dob";
    private static final String COLUMN_HOBBIES = "hobbies";
    private static final String COLUMN_AVATAR = "avatar";
    private static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + Friend_TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_FULL_NAME + " TEXT, " +
            COLUMN_GENDER + " TEXT, " +
            COLUMN_PHONE + " TEXT, " +
            COLUMN_DOB + " TEXT, " +
            COLUMN_HOBBIES + " TEXT, " +
            COLUMN_AVATAR + " TEXT)";

    private static final String EVENTS_TABLE_NAME = "events";
    private static final String EVENT_ID = "event_id";
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_LOCATION = "event_location";
    private static final String COLUMN_EVENT_DATE_TIME = "event_date_time";

    private static final String CREATE_EVENTS_TABLE = "CREATE TABLE " + EVENTS_TABLE_NAME + " (" +
            EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EVENT_NAME + " TEXT NOT NULL, " +
            EVENT_LOCATION + " TEXT, " +
            COLUMN_EVENT_DATE_TIME + " TEXT)";

    // New event_friends table to make link between two tables
    private static final String EVENT_FRIENDS_TABLE_NAME = "event_friends";
    private static final String EVENT_ID_COLUMN = "event_id";
    private static final String FRIEND_ID_COLUMN = "friend_id";

    private static final String CREATE_EVENT_FRIENDS_TABLE = "CREATE TABLE " + EVENT_FRIENDS_TABLE_NAME + " (" +
            EVENT_ID_COLUMN + " INTEGER, " +
            FRIEND_ID_COLUMN + " INTEGER, " +
            "FOREIGN KEY (" + EVENT_ID_COLUMN + ") REFERENCES " + EVENTS_TABLE_NAME + "(" + EVENT_ID + "), " +
            "FOREIGN KEY (" + FRIEND_ID_COLUMN + ") REFERENCES " + Friend_TABLE_NAME + "(" + COLUMN_ID + "), " +
            "PRIMARY KEY (" + EVENT_ID_COLUMN + ", " + FRIEND_ID_COLUMN + "))";
    // Constructor
    public DatabaseManager(Context c) {
        this.context = c;
        helper = new SQLHelper(c);
        this.db = helper.getWritableDatabase();
    }

    public DatabaseManager openReadable() throws android.database.SQLException {
        db = helper.getReadableDatabase();
        return this;
    }
    // Method to insert a new friend record into the database
    public boolean insertFriend(String name, String gender, String phone, String dob, String hobbies) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("phone", phone);
        values.put("dob", dob);
        values.put("hobbies", hobbies);

        try {
            db.insert(Friend_TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("Error in inserting rows", e.toString());
            e.printStackTrace();
            return false;
        }
        //db.close();
        return true;
    }

    // Method to update an existing friend record in the database
    public int updateFriend(long id, String name, String gender, String phone, String dob, String hobbies) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("gender", gender);
        values.put("phone", phone);
        values.put("dob", dob);
        values.put("hobbies", hobbies);

        return db.update(Friend_TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Method to delete a friend record from the database
    public int deleteFriend(String id) {
        return db.delete(Friend_TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Method to fetch all friend records from the database
    public Cursor fetchAllFriends() {
        return db.query(Friend_TABLE_NAME, new String[]{
                COLUMN_ID,
                COLUMN_FULL_NAME,
                COLUMN_GENDER,
                COLUMN_PHONE,
                COLUMN_DOB,
                COLUMN_HOBBIES,
                COLUMN_AVATAR}, null, null, null, null, null);
    }
    // Fetch specific friend details by id
    public Cursor fetchFriendById(long id) {
        return db.query(Friend_TABLE_NAME, new String[]{
                COLUMN_ID,
                COLUMN_FULL_NAME,
                COLUMN_GENDER,
                COLUMN_PHONE,
                COLUMN_DOB,
                COLUMN_HOBBIES,
                COLUMN_AVATAR
        }, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor searchFriendsByName(String query) {
        String selection = COLUMN_FULL_NAME + " LIKE ? OR " +
                COLUMN_PHONE + " LIKE ? OR " +
                COLUMN_HOBBIES + " LIKE ?";

        // Define the selection arguments
        String[] selectionArgs = new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"};

        // Execute the query
        return db.query(Friend_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    // Method to insert a new event record into the database
    public int insertEvent(String eventName, String eventLocation, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, eventName);
        values.put(EVENT_LOCATION, eventLocation);
        values.put(COLUMN_EVENT_DATE_TIME, dateTime);

        long result = -1;
        try {
            result = db.insert(EVENTS_TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("Error in inserting rows", e.toString());
            e.printStackTrace();
        }
        return (int) result;
    }
    public int updateEvent(long eventId, String name, String location, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(EVENT_NAME, name);
        values.put(COLUMN_EVENT_DATE_TIME, dateTime);
        values.put(EVENT_LOCATION, location);

        return db.update(EVENTS_TABLE_NAME, values, EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});
    }

    //,ethod to update number of participants in the event
    public boolean updateEventFriends(int eventId, ArrayList<Friend> friendIds) {
        // Remove existing friends
        db.delete(EVENT_FRIENDS_TABLE_NAME, EVENT_ID_COLUMN + " = ?", new String[]{String.valueOf(eventId)});
        // Add new friends
        return addFriendsToEvent(eventId, friendIds);
    }
    // Method to link friends to an event
    public boolean addFriendsToEvent(int eventId, ArrayList<Friend> friends) {
        ContentValues values = new ContentValues();
        try {
            for (Friend friend : friends) {
                values.clear();
                values.put(EVENT_ID_COLUMN, eventId);
                values.put(FRIEND_ID_COLUMN, friend.getId());
                db.insert(EVENT_FRIENDS_TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            Log.e("Error in adding friends to event", e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void deleteEventById(String eventId) {
        db.delete(EVENTS_TABLE_NAME, EVENT_ID + " = ?", new String[]{String.valueOf(eventId)});

    }
    public ArrayList<Friend> fetchFriendsForEvent(int eventId) {
        ArrayList<Friend> friends = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM friends WHERE id IN (SELECT friend_id FROM event_friends WHERE event_id = ?)", new String[]{String.valueOf(eventId)});
        Log.d("DatabaseQuery", "SELECT * FROM friends WHERE id IN (SELECT friend_id FROM event_friends WHERE event_id = " + eventId + ")");
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
    public Cursor fetchPastEvents() {
        String selection = COLUMN_EVENT_DATE_TIME + " < CURRENT_TIMESTAMP";
        return db.query(
                EVENTS_TABLE_NAME,
                null, // Retrieve all columns
                selection,
                null,
                null,
                null,
                COLUMN_EVENT_DATE_TIME + " DESC" // Order by dateTime
        );
    }

    // Method to fetch future events
    public Cursor fetchFutureEvents() {
        String selection = COLUMN_EVENT_DATE_TIME + " >= CURRENT_TIMESTAMP";
        return db.query(
                EVENTS_TABLE_NAME,
                null, // Retrieve all columns
                selection,
                null,
                null,
                null,
                COLUMN_EVENT_DATE_TIME + " ASC" // Order by dateTime
        );
    }
    // Method to close the database
    public void close() {
        helper.close();
    }

    public class SQLHelper extends SQLiteOpenHelper {
        public SQLHelper (Context c) {
            super(c, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_FRIENDS_TABLE);
            db.execSQL(CREATE_EVENTS_TABLE);
            db.execSQL(CREATE_EVENT_FRIENDS_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("Student table", "Upgrading database i.e. dropping table and re-creating it");
            db.execSQL("DROP TABLE IF EXISTS " + Friend_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EVENT_FRIENDS_TABLE_NAME);
            onCreate(db);
        }
    }
}
