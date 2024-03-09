package com.example.catering.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catering.Model.Restaurant;
import com.example.catering.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Restaurant> {
    public ListAdapter(@NonNull Context context, List<Restaurant> restaurant) {
        super(context, R.layout.list_item, restaurant);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Restaurant restaurant = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView listNomRestaurant = view.findViewById(R.id.listItemName);
        TextView listAdresseRestaurant = view.findViewById(R.id.address);
        listNomRestaurant.setText(restaurant.getNom());
        listAdresseRestaurant.setText(restaurant.getAdresse());


        return view;
    }
}
