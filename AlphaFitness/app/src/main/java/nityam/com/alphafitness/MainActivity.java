package nityam.com.alphafitness;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Configuration config = getResources().getConfiguration();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecordWorkout recordWorkout = new RecordWorkout();
            fragmentTransaction.replace(android.R.id.content,recordWorkout);
        } else {
            WorkoutDetails workoutDetails = new WorkoutDetails();
            fragmentTransaction.replace(android.R.id.content, workoutDetails);
        }
        fragmentTransaction.commit();
    }
}
