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

/**
 * Created by paulconroy on 15/12/2015.
 */
public class ReplyFragment extends Fragment {

    private TextView content;
    private Model mModel;
    private TextView confirmation;
    private TextView instruction;
    private Typeface typeFace;
    private Animation anim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.reply_fragment_layout,
                container, false);

        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/muli.ttf");

        confirmation = (TextView) myInflatedView.findViewById(R.id.confirmation);
        confirmation.setTypeface(typeFace);

        mModel = Model.getInstance();
        content = (TextView) myInflatedView.findViewById(R.id.reply);

        instruction = (TextView) myInflatedView.findViewById(R.id.instructionId);
        instruction.setVisibility(View.GONE);
        instruction.setTypeface(typeFace);

        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);



        content.setText("\"" + mModel.getReplyString() + "\"");
        content.setTypeface(typeFace);
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
