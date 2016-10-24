package com.doryapp.dory.extendedViews;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.doryapp.dory.fragments.CodeFragment;
import com.doryapp.dory.fragments.FriendsFragment;
import com.doryapp.dory.fragments.MapFragment;
import com.doryapp.dory.fragments.ProfileFragment;
import com.doryapp.dory.fragments.SettingsFragment;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {


    private Fragment[] pages = new Fragment[]{
            new ProfileFragment(),
            new MapFragment(),
            new FriendsFragment(),
            new CodeFragment(),
            new SettingsFragment()};

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
