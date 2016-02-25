package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class ContentFragment extends Fragment {

    private TextView content;
    private TextView sender;
    private Model mModel;
    private TextView instruction;
    private Typeface typeFace;
    private Animation anim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.content_fragment_layout,
                container, false);

        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muli.ttf");

        mModel = Model.getInstance();
        content = (TextView) myInflatedView.findViewById(R.id.content);
        sender = (TextView) myInflatedView.findViewById(R.id.senderId);
        instruction = (TextView) myInflatedView.findViewById(R.id.instructionId);

        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);

        instruction.setVisibility(View.GONE);
        instruction.setTypeface(typeFace);

        sender.setTypeface(typeFace);
        sender.setText(mModel.getSender());

        content.setTypeface(typeFace);
        content.setText("\"" + mModel.getMessageContent() + "\"");
        content.setMovementMethod(new ScrollingMovementMethod());

        setInstruction();


        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }

    public void setInstruction() {
        //FIX THIS, RUNS AFTER ACTIVITY IS CLOSED - USE ONSTOP METHOD REMOVE CALLBACKS.
        final Handler submitHandler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                instruction.setVisibility(View.VISIBLE);
                instruction.setAnimation(anim);
                anim.start();
                submitHandler.postDelayed(this, 3000);
                submitHandler.removeCallbacksAndMessages(null);
            }
        };

        submitHandler.postDelayed(r, 3000);

    }
}

