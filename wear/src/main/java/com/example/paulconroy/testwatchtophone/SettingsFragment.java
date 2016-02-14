package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class SettingsFragment extends Fragment {

    MainActivity control;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.settings_fragment_layout,
                container, false);


        control = new MainActivity();


        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }


}
