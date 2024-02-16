package com.example.catering;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import Model.Restaurant;
import Services.RestaurantService;

public class HomeActivity extends AppCompatActivity {

    private List<String> listeRestaurantsView = new ArrayList<>();
    private List<Restaurant> listeRestaurants = new ArrayList<>();

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
                    listeRestaurants.add(restaurant);
                }
                listeRestaurantsView = listeRestaurants.stream().map(Restaurant::getNom).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(homeActivity, android.R.layout.simple_list_item_1, listeRestaurantsView);
                listItems.setAdapter(adapter);
                listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        // Récupérer l'élément sur lequel l'utilisateur a cliqué
                        String selectedRestaurant = (String) adapterView.getItemAtPosition(position);

                        // Rediriger vers une autre activité
                        Intent intent = new Intent(homeActivity, MainActivity.class);
                        Restaurant restaurant = findRestaurantByNom(selectedRestaurant);
                        intent.putExtra("restaurant", restaurant); // Vous pouvez passer des données supplémentaires si nécessaire
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private Restaurant findRestaurantByNom(String nom){
        return listeRestaurants.stream().filter(restaurant -> restaurant.getNom().equals(nom)).findFirst().get();
    }
}
