package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class DeclineFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.decline_fragment_layout,
                container, false);


        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }
}
