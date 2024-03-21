package com.example.catering.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Reservation;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationRestaurantFragment extends Fragment {

    private Restaurant restaurant;
    private FirebaseService firebaseService = new FirebaseService();
    private UtilsService utilsService = new UtilsService();

    final List<Integer> nbPersonnesList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private CalendarView calendarView;
    private Spinner spinner;
    private EditText nom;
    private EditText prenom;
    private Date dateReservation = new Date();


    public ReservationRestaurantFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static ReservationRestaurantFragment newInstance(Restaurant restaurant) {
        ReservationRestaurantFragment fragment = new ReservationRestaurantFragment(restaurant);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservation_restaurant, container, false);

        spinner = view.findViewById(R.id.spinnerNbPersonnes);
        calendarView = view.findViewById(R.id.calendarViewReservation);
        nom = view.findViewById(R.id.nom);
        prenom = view.findViewById(R.id.prenom);
        TextView title = view.findViewById(R.id.nom_restaurant_reservation);

        title.setText(restaurant.getNom());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                setDate(year, month, day);
            }
        });

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, nbPersonnesList);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        Button getReservation = view.findViewById(R.id.buttonConfirmerReservation);
        getReservation.setOnClickListener(v -> {
            Reservation reservation = new Reservation(nom.getText().toString(), prenom.getText().toString(), (Integer) spinner.getSelectedItem(), dateReservation, restaurant.getId());
            firebaseService.createReservation(reservation, new DataCallBack<String>() {
                @Override
                public void onSuccess(String data) {
                    utilsService.replaceFragment(getParentFragmentManager(), new HomeFragment());
                    Log.d("Creation reservation", data);
                    Toast.makeText(getActivity().getApplicationContext(), "Réservation réussie", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(DatabaseError error) {
                    Log.e("Erreur lors de la creation de l'avis", error.toString());
                    Toast.makeText(getActivity().getApplicationContext(), "Echec réservation", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    private void setDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        this.dateReservation = calendar.getTime();
    }
}