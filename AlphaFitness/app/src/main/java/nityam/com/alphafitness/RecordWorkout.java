package nityam.com.alphafitness;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by nityamshrestha on 11/10/17.
 */

public class RecordWorkout extends Fragment {

    public RecordWorkout(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_workout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        Fragment distFrag = fragmentManager.findFragmentById(R.id.distancefrag);
        Fragment durFrag = fragmentManager.findFragmentById(R.id.durationfrag);
        Fragment mapFrag = fragmentManager.findFragmentById(R.id.mapwhere);

        if(distFrag == null){
            distFrag = new FragDistance();
            ;
            fragmentManager.beginTransaction()
                    .add(R.id.distancefrag, distFrag)
                    .commit();
        }


        if(durFrag == null){
            durFrag = new FragDuration();
            ;
            fragmentManager.beginTransaction()
                    .add(R.id.durationfrag, durFrag)
                    .commit();
        }

        if(mapFrag == null){
            mapFrag = new MapFrag();
            ;
            fragmentManager.beginTransaction()
                    .add(R.id.mapwhere, mapFrag)
                    .commit();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
