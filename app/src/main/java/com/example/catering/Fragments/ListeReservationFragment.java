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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listItems;

    private List<String> listeRestaurantsView = new ArrayList<>();

    private List<Reservation> listeReservations = new ArrayList<>();

    private UtilsService utilsService = new UtilsService();

    private FirebaseService firebaseService = new FirebaseService();

    public ListeReservationFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListeRestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListeReservationFragment newInstance(String param1, String param2) {
        ListeReservationFragment fragment = new ListeReservationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
