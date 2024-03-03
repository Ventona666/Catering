package com.example.catering.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.catering.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, container, false);
        Switch switchListMap = view.findViewById(R.id.switchListMap);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerHome, new ListeRestaurantFragment())
                .addToBackStack(null)
                .commit();

        switchListMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerHome, new MapsRestaurantFragment())
                            .addToBackStack(null)
                            .commit();
                }
                else{
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerHome, new ListeRestaurantFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}