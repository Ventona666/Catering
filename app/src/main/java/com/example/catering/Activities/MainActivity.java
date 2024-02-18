package com.example.catering.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.catering.Fragments.ListeRestaurantFragment;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.UtilsService;

public class MainActivity extends AppCompatActivity {

    private UtilsService utilsService = new UtilsService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            utilsService.replaceFragment(getSupportFragmentManager(), new ListeRestaurantFragment());
        }
    }

}