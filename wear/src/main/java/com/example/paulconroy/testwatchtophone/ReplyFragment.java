package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class ReplyFragment extends Fragment {

    private TextView content;
    private Model mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.reply_fragment_layout,
                container, false);

        mModel = Model.getInstance();
        content = (TextView) myInflatedView.findViewById(R.id.reply);

        content.setText("\"" + mModel.getReplyString() + "\"");


        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }
}
