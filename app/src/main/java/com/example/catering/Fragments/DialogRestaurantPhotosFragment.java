package com.example.catering.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.catering.Model.RestaurantPhotos;
import com.example.catering.R;
import com.example.catering.Utils.ListImageAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogAvisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogRestaurantPhotosFragment extends DialogFragment {

    private RestaurantPhotos restaurantPhotos;

    private TextView nomRestaurant;

    private RecyclerView listePhotos;

    public DialogRestaurantPhotosFragment(RestaurantPhotos restaurantPhotos) {
        this.restaurantPhotos = restaurantPhotos;
    }

    public static DialogRestaurantPhotosFragment newInstance(RestaurantPhotos restaurantPhotos) {
        DialogRestaurantPhotosFragment fragment = new DialogRestaurantPhotosFragment(restaurantPhotos);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_restaurant_photos, null);

        nomRestaurant = view.findViewById(R.id.nomRestaurant);
        nomRestaurant.setText(restaurantPhotos.getNomRestaurant());

        listePhotos = view.findViewById(R.id.listPhotos);
        listePhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        initPhotos();

        builder.setView(view);

        builder.setNegativeButton("Fermer", (dialog, id) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    private void initPhotos(){
        if(!restaurantPhotos.getFilePathPhotos().isEmpty()){
            ListImageAdapter listImageAdapter = new ListImageAdapter(getContext(), restaurantPhotos.getFilePathPhotos());
            listePhotos.setAdapter(listImageAdapter);
        }
    }
}