package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class MessageFragment extends Fragment {

    ImageButton messageBTN;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.message_fragment_layout,
                container, false);

        messageBTN = (ImageButton) myInflatedView.findViewById(R.id.messageBTN);


        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }


}
