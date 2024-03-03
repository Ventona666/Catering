package com.example.catering.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsRestaurantFragment extends Fragment {
    private FirebaseService firebaseService = new FirebaseService();
    private List<Restaurant> listeRestaurants = new ArrayList<>();
    private UtilsService utilsService = new UtilsService();
    private int latSum = 0;
    private int lonSum = 0;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            firebaseService.findAllRestaurants(data -> {
                listeRestaurants = data;

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        String title = marker.getTitle();
                        Restaurant restaurant = listeRestaurants.stream().filter(r -> r.getNom().equals(title)).findFirst().get();
                        if (getParentFragment() != null){
                            utilsService.replaceFragment(getParentFragment().getParentFragmentManager(), new RestaurantDetailFragment(restaurant));
                        }
                        return true;
                    }
                });

                initMarker(googleMap);
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_restaurant, container, false);
    }

    private void initMarker(GoogleMap googleMap){
        for (int i = 0; i < listeRestaurants.size(); i++){
            LatLng latLng = new LatLng(listeRestaurants.get(i).getLat(), listeRestaurants.get(i).getLon());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(listeRestaurants.get(i).getNom()));
            latSum += (int) listeRestaurants.get(i).getLat();
            lonSum += (int) listeRestaurants.get(i).getLon();
        }
        LatLng latLngMoy = new LatLng((double) latSum /listeRestaurants.size(), (double) lonSum / listeRestaurants.size());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngMoy));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}