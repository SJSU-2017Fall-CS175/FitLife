package nityam.com.fitlife;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UserProfile extends FragmentActivity {
    String sName;
    TextView name;
    Spinner genderSpinner;
    TextView weight;

    TextView userDistanceAvg;
    TextView timeAvg;
    TextView workoutCountAvg;
    TextView caloriesAvg;

    TextView userDistanceAll;
    TextView timeAll;
    TextView caloriesAll;
    TextView workoutCountAll;
    boolean freshopen;


    User user;
    UsersDBOperations operations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        operations = new UsersDBOperations(this);
        operations.open();
        user = new User();

        name = (TextView) findViewById(R.id.userName);
        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        weight = (TextView) findViewById(R.id.userWeight);

        userDistanceAvg = (TextView) findViewById(R.id.userDistanceAvg);
        userDistanceAll = (TextView) findViewById(R.id.userDistanceAll);
        timeAvg = (TextView) findViewById(R.id.timeAvg);
        timeAll = (TextView) findViewById(R.id.timeAll);
        workoutCountAvg = (TextView) findViewById(R.id.workoutCountAvg);
        workoutCountAll = (TextView) findViewById(R.id.workoutCountAll);
        caloriesAvg = (TextView) findViewById(R.id.caloriesAvg);
        caloriesAll = (TextView) findViewById(R.id.caloriesAll);

        sName = name.getText().toString();

        Toast.makeText(this, "Fresh value: "+ Boolean.toString(freshopen), Toast.LENGTH_SHORT).show();

       if(!freshopen){
           getUserInfo();
           getWeekData();
           getAllData();
       }
        //need to add spinner Gender

        // need to read from db as well

    }

    private void getUserInfo(){

//        Long number = operations.getLastUser();
//        Toast.makeText(this, Long.toString(number), Toast.LENGTH_SHORT).show();

        User myCurrentUser = operations.getUser(1);
        if(myCurrentUser == null){

        }else{
            name.setText(myCurrentUser.getmName());
            String gender = myCurrentUser.getmGender();
            if (gender.equalsIgnoreCase("Male")) {
                genderSpinner.setSelection(0);
            } else {
                genderSpinner.setSelection(1);
            }
            weight.setText(String.valueOf(myCurrentUser.getmWeight()));
        }


    }

    private void getWeekData(){
        List<UserData> weeklyData;

        weeklyData = operations.getWeeklyData();

        float totalDistance = 0;
        float totalTime = 0;
        int totalWorkouts = 0;
        float totalCalories = 0;

        for (UserData data: weeklyData) {
            //distance
            totalDistance += data.getmDistance_ran_in_a_week();
            //time
            totalTime += data.getmTime_ran_in_a_week();
            //workouts
            totalWorkouts = (int) data.getmWorkouts_done_in_a_week();
            //calories
            totalCalories += data.getmCalories_burned_in_a_week();
        }

        String timeInString = timeToString( (int) totalTime);

        String distance = String.format(java.util.Locale.US,"%.2f",totalDistance) + " miles";
        String workouts = String.valueOf(totalWorkouts) + " times";
        String calories = String.format(java.util.Locale.US,"%.2f",totalCalories) + " calories";

        userDistanceAvg.setText(distance);
        timeAvg.setText(timeInString);
        workoutCountAvg.setText(workouts);
        caloriesAvg.setText(calories);

    }


    private void getAllData(){

        List<UserData> allData;

        allData = operations.getAllData();
        float totalDistance = 0;
        float totalTime = 0;
        int totalWorkouts = 0;
        float totalCalories = 0;

        for (UserData data: allData) {
            //distance
            totalDistance += data.getmDistance_ran_in_a_week();
            //time
            totalTime += data.getmTime_ran_in_a_week();
            //workouts
            totalWorkouts = (int) data.getmWorkouts_done_in_a_week();
            //calories
            totalCalories += data.getmCalories_burned_in_a_week();
        }

        String timeInString = timeToString( (int) totalTime);

        String distance = String.format(java.util.Locale.US,"%.2f",totalDistance) + " miles";
        String workouts = String.valueOf(totalWorkouts) + " times";
        String calories = String.format(java.util.Locale.US,"%.2f",totalCalories) + " calories";

        userDistanceAll.setText(distance);
        timeAll.setText(timeInString);
        workoutCountAll.setText(workouts);
        caloriesAll.setText(calories);
    }

    @Override
    public void onBackPressed() {
        operations.close();
        freshopen = false;
        finish();
    }


    public void saveInfo(View view) {
        if (!name.getText().toString().isEmpty()) {
            user.setmName(name.getText().toString());
        }
        user.setmGender(genderSpinner.getSelectedItem().toString());

        if (!weight.getText().toString().isEmpty()) {
            user.setmWeight(Float.parseFloat(weight.getText().toString()));
        }

        operations.addUser(user);
        freshopen = false;

//        if((!mSavedUser.getmName().equalsIgnoreCase(user.getmName())) && !
//                name.getText().toString().isEmpty()) {
//            mSavedUser = mUserOps.addUser(newUser);
//        } else if (nameEditText.getText().toString().isEmpty()) {
//            TastyToast.makeText(this, "User adding failed! Try again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
//        } else {
//            TastyToast.makeText(this, "User " + newUser.getmName() + " was already in the DB. Try again",
//                    TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        operations.close();
    }

    private String timeToString (int mins) {
        int seconds = mins * 60;

        int minutes = (seconds % 3600) / 60;
        int hours = seconds / 3600;
        int days = seconds / 86400;
        seconds = (seconds % 3600) % 60;

        return days + " day " + hours + " hr " + minutes + " min " + seconds + " sec";
    }
}
