package com.example.catering;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import Model.Restaurant;
import Services.RestaurantService;

public class HomeActivity extends AppCompatActivity {

    private List<String> listeRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initRestaurantsList(this);
    }

    private void initRestaurantsList(HomeActivity homeActivity){
        RestaurantService restaurantService = new RestaurantService();
        Task<DataSnapshot> dataSnapshotTask = restaurantService.findAll();
        ListView listItems = homeActivity.findViewById(R.id.list_item);
        dataSnapshotTask.addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    listeRestaurants.add(restaurant.getNom());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(homeActivity, android.R.layout.simple_list_item_1, listeRestaurants);
                listItems.setAdapter(adapter);
            }
        });
    }
}
