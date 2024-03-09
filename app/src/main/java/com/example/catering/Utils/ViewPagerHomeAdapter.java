package com.example.catering.Utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.catering.Fragments.HomeFragment;
import com.example.catering.Fragments.ListeRestaurantFragment;
import com.example.catering.Fragments.MapsRestaurantFragment;

public class ViewPagerHomeAdapter extends FragmentStateAdapter {
    public ViewPagerHomeAdapter(@NonNull HomeFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new MapsRestaurantFragment();
        }
        return new ListeRestaurantFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
