package com.example.adi.futurebank;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter
{
    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        switch(position) {
            case 0:
                AccountFragment accountFragment = new AccountFragment();
                return accountFragment;

            case 1:
                TransactionsFragment transactionsFragment = new TransactionsFragment();
                return transactionsFragment;

            case 2:
                EventFragment eventsFragment = new EventFragment();
                return eventsFragment;

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
    public CharSequence getPageTitle(int position)
    {
        switch(position) {
            case 0:
                return "Account";

            case 1:
                return "Transactions";

            case 2:
                return "Events";

            default:
                return null;

        }
    }
}
