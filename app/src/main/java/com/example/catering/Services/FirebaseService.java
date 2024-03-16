package com.example.catering.Services;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Avis;
import com.example.catering.Model.Reservation;
import com.example.catering.Model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    private static final String URL_DATABASE = "https://catering-bdd-default-rtdb.europe-west1.firebasedatabase.app";

    private static final String RESTAURANT_REFERENCE = "restaurants";

    private static final String AVIS_REFERENCE = "avis";

    private static final String RESERVATION_REFERENCE = "reservation";


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
                    dataCallBack.onSuccess(restaurants);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataCallBack.onError(error);
            }
        });

    }

    public void findAllAvisByRestaurant(Long restaurantId, DataCallBack<List<Avis>> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(AVIS_REFERENCE);
        Query query = databaseReference.orderByChild("restaurantId").equalTo(restaurantId);
        final List<Avis> listeAvis = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listeAvis.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Avis avis = snapshot.getValue(Avis.class);
                    listeAvis.add(avis);
                }

                if (dataCallBack != null) {
                    dataCallBack.onSuccess(listeAvis);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataCallBack.onError(error);
            }
        });
    }

    public void createAvis(Avis avis, DataCallBack<String> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(AVIS_REFERENCE);
        String avisKey = databaseReference.push().getKey();
        avis.setId(avisKey);
        databaseReference.child(avisKey).setValue(avis, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    dataCallBack.onError(error);

                }else {
                    String messageSuccess = "L'ajout de l'avis a ete correctement effectue en base";
                    dataCallBack.onSuccess(messageSuccess);
                }
            }
        });
    }

    public void createReservation(Reservation reservation, DataCallBack<String> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(RESERVATION_REFERENCE);
        String referenceKey = databaseReference.push().getKey();
        reservation.setId(referenceKey);

        databaseReference.child(referenceKey).setValue(reservation, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    dataCallBack.onError(error);

                }else {
                    String messageSuccess = "L'ajout de l'avis a ete correctement effectue en base";
                    dataCallBack.onSuccess(messageSuccess);
                }
            }
        });
    }
}
