package nityam.com.myapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
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

public class HomePage extends FragmentActivity implements OnMapReadyCallback, SensorEventListener {
    private GoogleMap mMap;
    private boolean isWorkingOut = false;
    private Button workout;
    private TextView time;
    int Seconds, Minutes, MilliSeconds ;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;

    Handler handler;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("<NITYAM>", "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("<NITYAM>", "onStop: ");
        if(isWorkingOut){
            handler.postDelayed(runnable, 0);
        }
        sManager.unregisterListener(this, stepSensor);
    }

    @Override
    protected void onResume() {

        super.onResume();

        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
            // need to stop now
            workout.setText("STOP WORKOUT");
            workout.setBackgroundColor(Color.RED);
            isWorkingOut = true;


            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);

        }else{
            workout.setText("START WORKOUT");
            workout.setBackgroundColor(Color.GREEN);
            isWorkingOut = false;

            handler.removeCallbacks(runnable);
            String ans = "Total Workout = "+Integer.toString(Minutes) + "mins " + Integer.toString(Seconds) + " secs";
            Toast.makeText(this, ans , Toast.LENGTH_LONG).show();

            MillisecondTime = 0L ;
            StartTime = 0L ;
            TimeBuff = 0L ;
            UpdateTime = 0L ;
            Seconds = 0 ;
            Minutes = 0 ;
            MilliSeconds = 0 ;

            time.setText("0:00:00");
        }
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

            handler.postDelayed(this, 0);
        }

    };

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
