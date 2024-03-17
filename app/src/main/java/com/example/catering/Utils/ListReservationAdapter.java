package com.example.catering.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Reservation;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.google.firebase.database.DatabaseError;
import java.text.SimpleDateFormat;

import java.util.List;

public class ListReservationAdapter extends ArrayAdapter<Reservation> {
    private FirebaseService firebaseService = new FirebaseService();

    public ListReservationAdapter(@NonNull Context context, List<Reservation> reservations) {
        super(context, R.layout.list_reservation_item, reservations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_reservation_item, parent, false);
        }

        TextView listNomRestaurant = view.findViewById(R.id.listReservationItemName);
        TextView listAdresseRestaurant = view.findViewById(R.id.address_reservation);
        TextView listDate = view.findViewById(R.id.date_reservation);
        TextView listNbPersonnes = view.findViewById(R.id.nb_personne_reservation);
        firebaseService.findRestaurandById(reservation.getRestaurantId(), new DataCallBack<Restaurant>() {
            @Override
            public void onSuccess(Restaurant data) {
                listNomRestaurant.setText(data.getNom());
                listAdresseRestaurant.setText(data.getAdresse());
                listDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(reservation.getDate()));

                listNbPersonnes.setText(reservation.getNbPersonne().toString());
            }

            @Override
            public void onError(DatabaseError error) {
                Log.e("Erreur lors de la recherche", error.toString());
            }
        });



        return view;
    }
}
