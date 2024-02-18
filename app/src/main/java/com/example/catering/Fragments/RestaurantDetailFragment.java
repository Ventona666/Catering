package com.example.catering.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.UtilsService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Restaurant restaurant;

    private UtilsService utilsService = new UtilsService();

    public RestaurantDetailFragment(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantDetailFragment newInstance(String param1, String param2, Restaurant restaurant) {
        RestaurantDetailFragment fragment = new RestaurantDetailFragment(restaurant);
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

        View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        TextView nomRestaurant = view.findViewById(R.id.nom_restaurant);
        TextView descriptionRestaurant = view.findViewById(R.id.description_restaurant);
        nomRestaurant.setText(this.restaurant.getNom());
        descriptionRestaurant.setText(this.restaurant.getDescription());

        Button reservationButton = view.findViewById(R.id.reserver_button);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilsService.replaceFragment(getParentFragmentManager(), new ReservationRestaurantFragment(restaurant));
            }
        });

        return view;
    }
}