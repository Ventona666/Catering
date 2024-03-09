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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listItems;

    private List<String> listeRestaurantsView = new ArrayList<>();

    private List<Restaurant> listeRestaurants = new ArrayList<>();

    private UtilsService utilsService = new UtilsService();

    private FirebaseService firebaseService = new FirebaseService();

    public ListeRestaurantFragment() {

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
    public static ListeRestaurantFragment newInstance(String param1, String param2) {
        ListeRestaurantFragment fragment = new ListeRestaurantFragment();
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
                listeRestaurantsView = listeRestaurants.stream().map(Restaurant::getNom).collect(Collectors.toList());
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

    private Restaurant findRestaurantByNom(String nom){
        return listeRestaurants.stream().filter(restaurant -> restaurant.getNom().equals(nom)).findFirst().get();
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