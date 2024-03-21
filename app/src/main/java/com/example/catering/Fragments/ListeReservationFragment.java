package com.example.catering.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Reservation;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.example.catering.Utils.ListAdapter;
import com.example.catering.Utils.ListReservationAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListeReservationFragment extends Fragment {

    private ListView listItems;

    private List<Reservation> listeReservations = new ArrayList<>();

    private FirebaseService firebaseService = new FirebaseService();

    public ListeReservationFragment() {

    }

    public static ListeReservationFragment newInstance() {
        ListeReservationFragment fragment = new ListeReservationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_reservation, container, false);
        listItems = view.findViewById(R.id.list_reservation_item);

        Button getReservation = view.findViewById(R.id.buttonRechercherReservation);
        getReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nom = view.findViewById(R.id.nomRechercheReservation);
                EditText prenom = view.findViewById(R.id.prenomRechercheReservation);
                initReservationsList(view, nom.getText().toString(), prenom.getText().toString());
            }
        });


        return view;
    }

    private void initReservationsList(View view, String nom, String prenom){
        firebaseService.findReservationByNomAndPrenom(nom, prenom, new DataCallBack<List<Reservation>>() {
            @Override
            public void onSuccess(List<Reservation> data) {
                listeReservations = data;
                ListReservationAdapter adapter = new ListReservationAdapter(view.getContext(), listeReservations);
                if(adapter.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(), "Aucune réservation trouvée", Toast.LENGTH_SHORT).show();
                }
                listItems.setAdapter(adapter);
            }

            @Override
            public void onError(DatabaseError error){
                Log.e("Erreur lors de la recuperation des restaurants", error.toString());
            }
        });
    }
}
