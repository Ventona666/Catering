package com.example.catering.Services;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.catering.Fragments.RestaurantDetailFragment;
import com.example.catering.R;

public class UtilsService {

    public void replaceFragment(FragmentManager fM, Fragment fragment) {
        FragmentManager fragmentManager = fM;
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
