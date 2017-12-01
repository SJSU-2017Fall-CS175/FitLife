package nityam.com.fitlife;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailPage extends FragmentActivity {

    TextView avgDetail;
    TextView maxDetail;
    TextView minDetail;
    UsersDBOperations mUserOps;

    private CombinedChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;

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
        mChart = (CombinedChart) findViewById(R.id.chart1);

        mUserOps = new UsersDBOperations(this);
        mUserOps.open();

        getAverageMinMaxTimes();
        createChart();

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

    private void createChart() {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE });

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        CombinedData data = new CombinedData();
//        data.setData(getLineData());
        data.setData(generateBarData());

        mChart.setData(data);
        mChart.animateXY(2000,2000);
        mChart.invalidate();
    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        List<Float> distanceData = getDistanceData();

        for (int index = 0; index < distanceData.size(); index++) {
            entries.add(new BarEntry(index, distanceData.get(index)));
        }

        BarDataSet set1 = new BarDataSet(entries, "Calories");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        return new BarData(set1);
    }

    private List<Float> getDistanceData() {

        List<Float> returnList = new ArrayList<>();
        final int STEPS_IN_A_MILE = 2000;

        List <UserData> allData;
        allData = mUserOps.getAllData();

        for (UserData data: allData) {
            //get steps covered every 5 minutes
            returnList.add(data.getmDistance_ran_in_a_week() * STEPS_IN_A_MILE);
        }

        return returnList;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.combined, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionToggleLineValues: {
                for (IDataSet set : mChart.getData().getDataSets()) {
                    if (set instanceof LineDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleBarValues: {
                for (IDataSet set : mChart.getData().getDataSets()) {
                    if (set instanceof BarDataSet)
                        set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionRemoveDataSet: {

                int rnd = (int) getRandom(mChart.getData().getDataSetCount(), 0);
                mChart.getData().removeDataSet(mChart.getData().getDataSetByIndex(rnd));
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
                mChart.invalidate();
                break;
            }
        }
        return true;
    }

    private float getRandom(int maximum, int minimum) {
      return  minimum + (int)(Math.random() * maximum);
    }


}
