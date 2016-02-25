package com.example.paulconroy.testwatchtophone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Toast;

import adapter.FragmentAdapter;

/**
 * Created by paulconroy on 10/02/2016.
 */
public class DashBoard extends Activity {

    private ViewPager mViewPager;
    private GestureDetector mGestureDetector;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pager);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new FragmentAdapter(
                getFragmentManager());
        adapter.addFragment(new MessageFragment());
        adapter.addFragment(new SettingsFragment());

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

        mViewPager.setCurrentItem(0);


    }

    public void commitFunction(int position) {
        if (position == 0) {
//            Toast.makeText(this, "Reply Function", Toast.LENGTH_LONG).show();

        } else if (position == 1) {
//            Toast.makeText(this, "Decline Function", Toast.LENGTH_LONG).show();

        }
    }

    public void messageBTNHandler(View v) {
        Toast.makeText(this, "Message btn clicked", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Messages.class);
        startActivity(i);
        overridePendingTransition(R.anim.open_trans, R.anim.close_trans);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finish();
    }
}

