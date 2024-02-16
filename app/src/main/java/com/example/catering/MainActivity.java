package com.example.catering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Model.Restaurant;
import Services.RestaurantService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_detail);
        Intent intent = getIntent();
        Restaurant restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        TextView nomRestaurant = findViewById(R.id.nom_restaurant);
        TextView descriptionRestaurant = findViewById(R.id.description_restaurant);
        nomRestaurant.setText(restaurant.getNom());
        descriptionRestaurant.setText(restaurant.getDescription());
    }
}