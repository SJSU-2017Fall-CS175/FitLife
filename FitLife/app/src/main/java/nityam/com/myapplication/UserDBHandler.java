package nityam.com.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A class that manages all the DB
 * for the user class
 */

public class UserDBHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    static final String TABLE_USERS = "users";
    static final String COLUMN_ID = "userID";
    static final String COLUMN_NAME = "name";
    static final String COLUMN_GENDER = "gender";
    static final String COLUMN_WEIGHT= "weight";

    static final String TABLE_USER_DATA = "userData";
    static final String COLUMN_DISTANCE_WEEKLY= "workoutDistance";
    static final String COLUMN_TIME_WEEKLY= "workoutTime";
    static final String COLUMN_WORKOUTS_WEEKLY= "numWorkouts";
    static final String COLUMN_CALORIES_WEEKLY= "workoutCalories";

    private static final String USER_TABLE =
            " CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_GENDER + " TEXT, " +
                    COLUMN_WEIGHT + " NUMERIC" + ")";

    private static final String USER_DATA_TABLE =
            " CREATE TABLE " + TABLE_USER_DATA + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DISTANCE_WEEKLY + " NUMERIC, " +
                    COLUMN_TIME_WEEKLY + " NUMERIC, " +
                    COLUMN_WORKOUTS_WEEKLY + " NUMERIC, " +
                    COLUMN_CALORIES_WEEKLY + " NUMERIC" + ")";

    UserDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE);
        db.execSQL(USER_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS "+ TABLE_USERS);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        db.execSQL(USER_TABLE);
        db.execSQL(USER_DATA_TABLE);
    }
}
