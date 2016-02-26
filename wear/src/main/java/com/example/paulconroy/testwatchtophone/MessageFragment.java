package com.example.paulconroy.testwatchtophone;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by paulconroy on 15/12/2015.
 */
public class MessageFragment extends Fragment {

    ImageButton messageBTN;
    ImageView circleBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.message_fragment_layout,
                container, false);

        messageBTN = (ImageButton) myInflatedView.findViewById(R.id.messageBTN);
        circleBack = (ImageView) myInflatedView.findViewById(R.id.circleback);

        final Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_up);
        anim.setRepeatCount(Animation.INFINITE);
        circleBack.setAnimation(anim);
        anim.start();

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                circleBack.clearAnimation();
                circleBack.setAnimation(anim);
                anim.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });










        return myInflatedView;
    }

    public static void stopCycling() {
        // TODO Auto-generated method stub

    }


}
