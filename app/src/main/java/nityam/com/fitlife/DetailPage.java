package nityam.com.fitlife;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class DetailPage extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        Configuration config = getResources().getConfiguration();
        if(config.orientation == config.ORIENTATION_PORTRAIT){
            super.onBackPressed();
            finish();
        }

        // need to read from db as well
    }
}
