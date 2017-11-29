package nityam.com.fitlife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to do CRUD operations
 * on the database
 */

@SuppressWarnings("unused")
public class UsersDBOperations {

    private static final String LOGTAG = "USER_MNGMNT_SYS";

    private final SQLiteOpenHelper dbhandler;
    private SQLiteDatabase database;

    private static final String[] allUserColumns = {
            UserDBHandler.COLUMN_ID,
            UserDBHandler.COLUMN_NAME,
            UserDBHandler.COLUMN_GENDER,
            UserDBHandler.COLUMN_WEIGHT
    };

    private static final String[] allUserDataColumns = {
            UserDBHandler.COLUMN_ID,
            UserDBHandler.COLUMN_DISTANCE_WEEKLY,
            UserDBHandler.COLUMN_TIME_WEEKLY,
            UserDBHandler.COLUMN_WORKOUTS_WEEKLY,
            UserDBHandler.COLUMN_CALORIES_WEEKLY
    };

    public UsersDBOperations(Context context) {
        dbhandler = new UserDBHandler(context);
    }

    public void open() {
        Log.i(LOGTAG, "Database Opened");
        database = dbhandler.getWritableDatabase();
    }

    public void close() {
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    public User addUser(User userToAdd) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_NAME, userToAdd.getmName());
        values.put(UserDBHandler.COLUMN_GENDER, userToAdd.getmGender());
        values.put(UserDBHandler.COLUMN_WEIGHT, userToAdd.getmWeight());
        long insertId = database.insert(UserDBHandler.TABLE_USERS, null, values);
        userToAdd.setmId(insertId);
        return userToAdd;
    }

    public UserData addUserData(UserData userDataToAdd) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_DISTANCE_WEEKLY, userDataToAdd.getmDistance_ran_in_a_week());
        values.put(UserDBHandler.COLUMN_TIME_WEEKLY, userDataToAdd.getmTime_ran_in_a_week());
        values.put(UserDBHandler.COLUMN_WORKOUTS_WEEKLY, userDataToAdd.getmWorkouts_done_in_a_week());
        values.put(UserDBHandler.COLUMN_CALORIES_WEEKLY, userDataToAdd.getmCalories_burned_in_a_week());
        long insertID = database.insert(UserDBHandler.TABLE_USER_DATA, null, values);
        userDataToAdd.setmId(insertID);
        return userDataToAdd;
    }

    public int updateUserData(UserData userData) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_DISTANCE_WEEKLY, userData.getmDistance_ran_in_a_week());
        values.put(UserDBHandler.COLUMN_TIME_WEEKLY, userData.getmTime_ran_in_a_week());
        values.put(UserDBHandler.COLUMN_WORKOUTS_WEEKLY, userData.getmWorkouts_done_in_a_week());
        values.put(UserDBHandler.COLUMN_CALORIES_WEEKLY, userData.getmCalories_burned_in_a_week());

        return database.update(UserDBHandler.TABLE_USER_DATA, values,
                UserDBHandler.COLUMN_ID + "=?",new String[] { String.valueOf(userData.getmId())});
    }

    public List<UserData> getWeeklyData() {

        Cursor cursor = database.query(UserDBHandler.TABLE_USER_DATA, allUserDataColumns,
                null,null,null, null, null);

        int currentCount = 0;
        List<UserData> weeklyUserData = new ArrayList<>();

        if (cursor.getCount() > 0) {
            int DAYS_IN_A_WEEK = 7;
            while (cursor.moveToNext() && currentCount < DAYS_IN_A_WEEK) {
                UserData userData = new UserData();
                userData.setmId((cursor.getLong(cursor.getColumnIndex(UserDBHandler.COLUMN_ID))));
                userData.setmDistance_ran_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_DISTANCE_WEEKLY)));
                userData.setmTime_ran_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_TIME_WEEKLY)));
                userData.setmWorkouts_done_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_WORKOUTS_WEEKLY)));
                userData.setmCalories_burned_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_CALORIES_WEEKLY)));

                weeklyUserData.add(userData);
                currentCount++;

            }
        }
        cursor.close();

        return weeklyUserData;
    }

    public List<UserData> getAllData() {

        Cursor cursor = database.query(UserDBHandler.TABLE_USER_DATA, allUserDataColumns,
                null,null,null, null, null);

        List<UserData> allUserData = new ArrayList<>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                UserData userData = new UserData();
                userData.setmId((cursor.getLong(cursor.getColumnIndex(UserDBHandler.COLUMN_ID))));
                userData.setmDistance_ran_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_DISTANCE_WEEKLY)));
                userData.setmTime_ran_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_TIME_WEEKLY)));
                userData.setmWorkouts_done_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_WORKOUTS_WEEKLY)));
                userData.setmCalories_burned_in_a_week(cursor.getFloat(cursor.getColumnIndex(UserDBHandler.COLUMN_CALORIES_WEEKLY)));

                allUserData.add(userData);

            }
        }

        cursor.close();

        return allUserData;
    }

    public User getLastUser(){
        Cursor cursor = database.query(UserDBHandler.TABLE_USERS, allUserColumns,
                UserDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(1)},
                null,null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        User user = new User(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Float.parseFloat(cursor.getString(3)));

        cursor.close();

        return user;
    }

    //Get a single user
    public User getUser(long id) {
        Cursor cursor = database.query(UserDBHandler.TABLE_USERS, allUserColumns,
                UserDBHandler.COLUMN_ID + "=?",new String[]{String.valueOf(id)},
                null,null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        assert cursor != null;
        User user = new User(Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Float.parseFloat(cursor.getString(3)));

         cursor.close();

        return user;
    }

    public int updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDBHandler.COLUMN_NAME, user.getmName());
        values.put(UserDBHandler.COLUMN_GENDER, user.getmGender());
        values.put(UserDBHandler.COLUMN_WEIGHT, user.getmWeight());

        return database.update(UserDBHandler.TABLE_USERS, values,
                UserDBHandler.COLUMN_ID + "=?",
                new String[] { String.valueOf(user.getmId())});

    }

    public void removeUser(User user) {
        database.delete(UserDBHandler.TABLE_USERS,
                UserDBHandler.COLUMN_ID + "=" + user.getmId(), null);
    }


}
