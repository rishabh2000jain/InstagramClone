package com.example.instagram;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i)
        {
            case 0:
                return new Profile();
            case 1:
                return new User();
            case 2:
                return new SharePicture();
                default:
                    return null;

        }
      }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Profile";
            case 1:
                return "User";
            case 2:
                return "SharePictuer";
                default:
                    return null;
        }
    }
}
