package com.doryapp.dory.extendedViews;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.doryapp.dory.fragments.MapFragment;
import com.doryapp.dory.fragments.ScreenSlidePageFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


    private Fragment[] pages = new Fragment[]{new ScreenSlidePageFragment(), new ScreenSlidePageFragment(), new MapFragment(), new ScreenSlidePageFragment(), new ScreenSlidePageFragment()};

    public ScreenSlidePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pages[position];
    }

    @Override
    public int getCount() {
        return pages.length;
    }
}
