package nityam.com.fitlife;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.Random;

public class HomePage extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {
    private final int REQUEST_CODE = 0;

    private GoogleMap mMap;
    private boolean isWorkingOut = false;
    private Button workout;
    private TextView time;
    int Seconds, Minutes, MilliSeconds ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    private long steps = 0;
    UsersDBOperations mUserOps;

    private int numWorkouts = 0; // update this from db
    private UserData mUserData;

    private TextView distance;
    Random rn;

    SensorManager sManager;
    Sensor stepSensor;

    Handler handler;
    DecimalFormat df;

    private boolean isFirstLaunch = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Configuration config = getResources().getConfiguration();
        if(config.orientation == config.ORIENTATION_LANDSCAPE){
            gotoDetail(this);
        }

        workout = (Button) findViewById(R.id.workout);
        handler = new Handler() ;
        time = (TextView) findViewById(R.id.time);
        distance=(TextView) findViewById(R.id.distance);

        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

        mUserData = new UserData();
        mUserOps = new UsersDBOperations(this);
        rn = new Random();

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("<NITYAM>homePage", "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("<NITYAM>homePage", "onStop: ");
        sManager.unregisterListener(this, stepSensor);
        handler.removeCallbacks(writeToDBRunnable);
        mUserOps.close();
    }

    @Override
    protected void onResume() {

        super.onResume();

        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mUserOps.open();
        Log.d("<NITYAM>homePage", "onResume: ");

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);

        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);

        }

        LocationManager locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location l = locManager != null ? locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;

        if (l == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locManager != null ? locManager.getBestProvider(criteria, true) : null;
            assert locManager != null;
            l = locManager.getLastKnownLocation(provider);

            if (isFirstLaunch) {
                isFirstLaunch = false;
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }

        } else {

            if (isFirstLaunch) {
                isFirstLaunch = false;
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(l.getLatitude(), l.getLongitude())).title("Current location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(l.getLatitude(), l.getLongitude()), 16));
            } else {
                isFirstLaunch = false;
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(l.getLatitude(), l.getLongitude())));
            }
        }

    }

    public void uerProfileClicked(View view) {
        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    public void gotoDetail(HomePage view){
        Intent intent = new Intent(this, DetailPage.class);
        startActivity(intent);
    }

    public void btnActivityClicked(View view) {
        if(!isWorkingOut){
            // i'm working out now
            MillisecondTime = 0L ;
            StartTime = 0L ;
            TimeBuff = 0L ;
            UpdateTime = 0L ;
            Seconds = 0 ;
            Minutes = 0 ;
            MilliSeconds = 0 ;

            steps = 0;

            workout.setText("STOP WORKOUT");
            workout.setBackgroundColor(Color.RED);
            workout.setTextColor(Color.WHITE);
            isWorkingOut = true;


            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            handler.removeCallbacks(writeToDBRunnable);



        }else{
            // i've stoped my workout
            workout.setText("START WORKOUT");
            workout.setBackgroundColor(Color.GREEN);
            workout.setTextColor(Color.BLACK);
            isWorkingOut = false;

            handler.removeCallbacks(runnable);

//            this.steps = 67*Minutes + (int)(1.16 * Seconds);
                this.steps = 67*Seconds + 67*10*Minutes;

            String ans = "Total Workout = "+Integer.toString(Minutes) + "mins " + Integer.toString(Seconds) + " secs with steps: "+steps;
            Toast.makeText(this, ans , Toast.LENGTH_SHORT).show();

            //randomize steps
//            this.steps = 10 + rn.nextInt(4000 - 5 +1);//debug



//            Log.d("<NITYAMsteps++>", Long.toString(steps));

            distance.setText(df.format(getDistanceRun())); //should be on runnable

            handler.postDelayed(writeToDBRunnable, 0);

            time.setText("0:00:00");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
    }

    public float getDistanceRun(){
        float distance = (float)(steps*78)/(float)100000;
        return distance;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            time.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));



//            distance.setText(df.format(getDistanceRun()));

            handler.postDelayed(this, 0);
        }

    };

    private Runnable writeToDBRunnable = new Runnable() {
        public void run() {

            final int CALORIES_BURNED_PER_2000_STEPS = 120;
            Float distanceRan = getDistanceRun();
            Log.d("<NITYAM>HP_Distance", distanceRan.toString());
            Float workoutTime = 5f;
            numWorkouts++;
            Float workoutCalories = getCalories();

            mUserData.setmDistance_ran_in_a_week(distanceRan);
            mUserData.setmTime_ran_in_a_week(workoutTime);
            mUserData.setmWorkouts_done_in_a_week(numWorkouts);
            mUserData.setmCalories_burned_in_a_week(workoutCalories);

            mUserOps.addUserData(mUserData);

            handler.postDelayed(this, 300000);

            // need to read from db as well on the other activity
        }

    };

    /**
     * https://www.livestrong.com/article/40966-omron-walking-style-pedometer-instructions/
     */
    public float getCalories(){
        //((0.5 * weight ) /1400 ) * total Steps
        //get weight from database, default = 160;
        int weight = 160;
        float calorie = (float)(0.5 * weight)/1400;
        float totalCalories = calorie * steps;
        return totalCalories;
    }

}
