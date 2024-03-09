package com.example.catering.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    private static final String URL_DATABASE = "https://catering-bdd-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String RESTAURANT_REFERENCE = "restaurants";


    private FirebaseDatabase firebaseDatabase;

    public FirebaseService(){
        firebaseDatabase = FirebaseDatabase.getInstance(URL_DATABASE);
    }

    public void findAllRestaurants(DataCallBack<List<Restaurant>> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(RESTAURANT_REFERENCE);
        final List<Restaurant> restaurants = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurants.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    restaurants.add(restaurant);
                }

                if (dataCallBack != null) {
                    dataCallBack.onDataReceived(restaurants);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error database", error.toString());
            }
        });


    }
}
