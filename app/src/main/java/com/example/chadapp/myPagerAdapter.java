package com.example.chadapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class myPagerAdapter extends FragmentStatePagerAdapter {
    public myPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                GroupFragment groupFragment = new GroupFragment();
                return groupFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    //@Nullable
    //@Override
    //public CharSequence getPageTitle(int position) {
      //  switch(position)
        //{
          //  case 1:

        //}
   // }
}
