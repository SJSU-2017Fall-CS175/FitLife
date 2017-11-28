package nityam.com.fitlife;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class DetailPage extends FragmentActivity {

    TextView avgDetail;
    TextView maxDetail;
    TextView minDetail;
    UsersDBOperations mUserOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        Configuration config = getResources().getConfiguration();
        if(config.orientation == config.ORIENTATION_PORTRAIT){
            super.onBackPressed();
            finish();
        }

        avgDetail = (TextView) findViewById(R.id.averageDetail);
        maxDetail = (TextView) findViewById(R.id.maxDetail);
        minDetail = (TextView) findViewById(R.id.minDetail);

        mUserOps = new UsersDBOperations(this);
        mUserOps.open();

        getAverageMinMaxTimes();

        // get info from DB on the runnable
    }

    @Override
    protected void onPause() {
        super.onPause();
        //kill runnable
    }

    private void getAverageMinMaxTimes() {
        List<UserData> allData = mUserOps.getAllData();

        float totalDistance = 0f;
        float totalTime = 0f;

        float maxDistance = 0f;
        float maxTime = 0f;

        float minDistance = 0f;
        float minTime = 0f;

        for (int i = 0; i < allData.size() - 1; i++) {

            //distance
            totalDistance += allData.get(i).getmDistance_ran_in_a_week();
            maxDistance = allData.get(i).getmDistance_ran_in_a_week();
            if (allData.get(i + 1).getmDistance_ran_in_a_week() > maxDistance)
                maxDistance = allData.get(i + 1).getmDistance_ran_in_a_week();

            minDistance = allData.get(i).getmDistance_ran_in_a_week();
            if (allData.get(i + 1).getmDistance_ran_in_a_week() < maxDistance)
                minDistance = allData.get(i + 1).getmDistance_ran_in_a_week();

            //time
            totalTime += allData.get(i).getmTime_ran_in_a_week();
            maxTime = allData.get(i).getmTime_ran_in_a_week();
            if (allData.get(i + 1).getmTime_ran_in_a_week() > maxTime)
                maxTime = allData.get(i + 1).getmTime_ran_in_a_week();

            minTime = allData.get(i).getmTime_ran_in_a_week();
            if (allData.get(i + 1).getmTime_ran_in_a_week() < minTime)
                minTime = allData.get(i + 1).getmTime_ran_in_a_week();
        }


        float average = (totalTime / totalDistance);

//        String averageString =  String.valueOf(average);

        float min = (minTime / minDistance);
//        String minString = String.valueOf(min);
        float max = (maxTime / maxDistance);
//        String maxString =  String.valueOf(max);

        if(min> max){
            float temp = min;
            min = max;
            max = temp;
        }

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

//        avgDetail.setText(df.format(getDistanceRun()));

        avgDetail.setText(df.format(average));
        minDetail.setText(df.format(min));
        maxDetail.setText(df.format(max));
    }
}
