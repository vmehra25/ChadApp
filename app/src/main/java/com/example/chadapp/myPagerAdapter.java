package com.example.chadapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class myPagerAdapter extends FragmentStatePagerAdapter {

    Fragment frag1,frag2;

    public myPagerAdapter(FragmentManager fm,Fragment frag1,Fragment frag2) {
        super(fm);
        this.frag1 = frag1;
        this.frag2 = frag2;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i)
        {
            case 0:
                return frag1;
            case 1:
                return frag2;
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
