package nityam.com.myapplication;

/**
 * A class that represents all the user's data
 */

public class UserData {

    private long mId;
    private float mDistance_ran_in_a_week;
    private float mTime_ran_in_a_week;
    private float mWorkouts_done_in_a_week;
    private float mCalories_burned_in_a_week;

    public UserData() {

    }

    public long getmId() {
        return mId;
    }

    public void setmId(long Id) {
        this.mId = Id;
    }

    public float getmDistance_ran_in_a_week() {
        return mDistance_ran_in_a_week;
    }

    public void setmDistance_ran_in_a_week(float Distance_ran_in_a_week) {
        this.mDistance_ran_in_a_week = Distance_ran_in_a_week;
    }

    public float getmTime_ran_in_a_week() {
        return mTime_ran_in_a_week;
    }

    public void setmTime_ran_in_a_week(float Time_ran_in_a_week) {
        this.mTime_ran_in_a_week = Time_ran_in_a_week;
    }

    public float getmWorkouts_done_in_a_week() {
        return mWorkouts_done_in_a_week;
    }

    public void setmWorkouts_done_in_a_week(float Workouts_done_in_a_week) {
        this.mWorkouts_done_in_a_week = Workouts_done_in_a_week;
    }

    public float getmCalories_burned_in_a_week() {
        return mCalories_burned_in_a_week;
    }

    public void setmCalories_burned_in_a_week(float Calories_burned_in_a_week) {
        this.mCalories_burned_in_a_week = Calories_burned_in_a_week;
    }
}
