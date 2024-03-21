package com.example.catering.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.catering.Common.DataCallBack;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.google.firebase.database.DatabaseError;
import com.example.catering.Utils.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListeRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeRestaurantFragment extends Fragment {

    private ListView listItems;

    private List<Restaurant> listeRestaurants = new ArrayList<>();

    private UtilsService utilsService = new UtilsService();

    private FirebaseService firebaseService = new FirebaseService();

    public ListeRestaurantFragment() {

    }

    public static ListeRestaurantFragment newInstance() {
        ListeRestaurantFragment fragment = new ListeRestaurantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste_restaurant, container, false);
        listItems = view.findViewById(R.id.list_item);
        initRestaurantsList(view);
        return view;
    }

    private void initRestaurantsList(View view){
        firebaseService.findAllRestaurants(new DataCallBack<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> data) {
                listeRestaurants = data;
                ListAdapter adapter = new ListAdapter(view.getContext(), listeRestaurants);
                listItems.setAdapter(adapter);
                listItems.setOnItemClickListener(onClickListeRestaurant());
            }

            @Override
            public void onError(DatabaseError error){
                Log.e("Erreur lors de la recuperation des restaurants", error.toString());
            }
        });
    }

    private AdapterView.OnItemClickListener onClickListeRestaurant(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Restaurant restaurant = (Restaurant) adapterView.getItemAtPosition(position);
                if(getParentFragment() != null){
                    utilsService.replaceFragment(getParentFragment().getParentFragmentManager(), new RestaurantDetailFragment(restaurant));
                }
            }
        };
    }

}