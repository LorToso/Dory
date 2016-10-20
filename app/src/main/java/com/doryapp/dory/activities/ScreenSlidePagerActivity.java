package com.doryapp.dory.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.doryapp.dory.R;
import com.doryapp.dory.extendedViews.ScreenSlidePagerAdapter;
import com.doryapp.dory.fragments.MapFragment;
import com.doryapp.dory.fragments.ScreenSlidePageFragment;


public class ScreenSlidePagerActivity extends Activity {

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void onClickTabMe(View view) {
        mPager.setCurrentItem(0);
    }

    public void onClickTabMap(View view) {
        mPager.setCurrentItem(1);
    }

    public void onClickTabFriends(View view) {
        mPager.setCurrentItem(2);
    }

    public void onClickTabCode(View view) {
        mPager.setCurrentItem(3);
    }

    public void onClickTabOptions(View view) {
        mPager.setCurrentItem(4);
    }
}