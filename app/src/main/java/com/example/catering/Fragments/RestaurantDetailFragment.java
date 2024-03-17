package com.example.catering.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Avis;
import com.example.catering.Model.AvisView;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.example.catering.Utils.ListAvisAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantDetailFragment extends Fragment {

    private int nbAvis = 0;

    private Restaurant restaurant;

    private List<Avis> listeAvis = new ArrayList<>();

    private ListView listItems;

    private UtilsService utilsService = new UtilsService();

    private FirebaseService firebaseService = new FirebaseService();

    public RestaurantDetailFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public static RestaurantDetailFragment newInstance(String param1, String param2, Restaurant restaurant) {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment(restaurant);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        initDetailRestaurant(view);

        listItems = view.findViewById(R.id.liste_avis_restaurant);

        initAvisRestaurant(view);

        Button reservationButton = view.findViewById(R.id.reserver_button);
        reservationButton.setOnClickListener(onClickReserverButton());

        Button ajouterAvisButton = view.findViewById(R.id.ajouter_avis_button);
        ajouterAvisButton.setOnClickListener(onClickAjouterAvisButton());

        return view;
    }

    private void initDetailRestaurant(View view){

        TextView nomRestaurant = view.findViewById(R.id.nom_restaurant);
        TextView descriptionRestaurant = view.findViewById(R.id.description_restaurant);
        TextView adresseRestaurant = view.findViewById(R.id.adresse_restaurant);
        TextView mailRestaurant = view.findViewById(R.id.mail_restaurant);
        TextView telephoneRestaurant = view.findViewById(R.id.telephone_restaurant);

        nomRestaurant.setText(this.restaurant.getNom());
        adresseRestaurant.setText(this.restaurant.getAdresse() + ", " + this.restaurant.getCodePostal());
        mailRestaurant.setText(this.restaurant.getMail());
        telephoneRestaurant.setText(this.restaurant.getTelephone());
        descriptionRestaurant.setText(this.restaurant.getDescription());
    }

    private void initAvisRestaurant(View view){
        firebaseService.findAllAvisByRestaurant(restaurant.getId(), new DataCallBack<List<Avis>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(List<Avis> data) {
                listeAvis = data;
                nbAvis = listeAvis.size();
                TextView libelleAvis = view.findViewById(R.id.libelle_avis);
                libelleAvis.setText("Avis (" + nbAvis + ")");
                ListAvisAdapter listAvisAdapter = new ListAvisAdapter(view.getContext(), listeAvis);
                listItems.setAdapter(listAvisAdapter);
                listItems.setOnItemClickListener(onClickAvis());
            }

            @Override
            public void onError(DatabaseError error){
                Log.e("Erreur lors de la recuperation des avis", error.toString());
            }
        });;
    }

    private View.OnClickListener onClickReserverButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilsService.replaceFragment(getParentFragmentManager(), new ReservationRestaurantFragment(restaurant));
            }
        };

    }

    private View.OnClickListener onClickAjouterAvisButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilsService.replaceFragment(getParentFragmentManager(), new CreationAvisRestaurantFragment(restaurant));
            }
        };

    }

    private AdapterView.OnItemClickListener onClickAvis(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Avis avis = (Avis) parent.getItemAtPosition(position);
                DialogAvisFragment dialogFragment = new DialogAvisFragment(avis);
                dialogFragment.show(getChildFragmentManager(), "DialogAvisFragment");
            }
        };
    }
}