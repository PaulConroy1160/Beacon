package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import adapter.FragmentAdapter;


public class MainActivity extends Activity {

    private ViewPager mViewPager;
    private GestureDetector mGestureDetector;
    private FragmentAdapter adapter;
    private String messageText;
    private String sender;
    private Model mModel;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pager);

        mModel = Model.getInstance();

        messageText = getIntent().getStringExtra("message");
        sender = getIntent().getStringExtra("sender");

        mModel.setMessageContent(messageText);
        mModel.setSender(sender);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);


        mViewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new FragmentAdapter(
                getFragmentManager());
        adapter.addFragment(new AcceptFragment());
        adapter.addFragment(new ContentFragment());
        adapter.addFragment(new DeclineFragment());

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                commitFunction(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);


    }

    public void commitFunction(int position) {
        if (position == 0) {
            //Toast.makeText(this, "Reply Function", Toast.LENGTH_LONG).show();
            //mViewPager.setCurrentItem(1);
            sendReply();
            //Add sent message animation
        } else if (position == 2) {
            dismissMessage();
        }
    }


    public void sendReply() {
        Intent i = new Intent(this, SendMessage.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        mViewPager.setCurrentItem(1);
    }

    public void dismissMessage() {
        Intent i = new Intent(this, DashBoard.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
        this.finish();
    }
}



