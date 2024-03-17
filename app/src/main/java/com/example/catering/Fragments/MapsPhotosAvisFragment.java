package com.example.catering.Fragments;

import android.media.ExifInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.catering.Model.RestaurantPhotos;
import com.example.catering.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsPhotosAvisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsPhotosAvisFragment extends Fragment {

    private List<RestaurantPhotos> listeRestaurantPhotos = new ArrayList<>();

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
            initListeRestaurantPhotos();
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    String title = marker.getTitle();
                    RestaurantPhotos restaurantPhotos = listeRestaurantPhotos.stream().filter(r -> r.getNomRestaurant().equals(title)).findFirst().orElse(null);
                    if (restaurantPhotos != null) {
                        DialogRestaurantPhotosFragment dialogFragment = new DialogRestaurantPhotosFragment(restaurantPhotos);
                        dialogFragment.show(getChildFragmentManager(), "DialogRestaurantPhotosFragment");
                    }
                    return true;
                }
            });

            initMarker(googleMap);

            float zoomLevel = 14.0f;
            // Coordonnées brutes d'Agen
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.205, 0.6206), zoomLevel));
        }
    };

    public MapsPhotosAvisFragment() {
        // Required empty public constructor
    }

    public static MapsPhotosAvisFragment newInstance(String param1, String param2) {
        MapsPhotosAvisFragment fragment = new MapsPhotosAvisFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapPhotos);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps_photos_avis, container, false);
    }

    private void initMarker(GoogleMap googleMap){
        for (int i = 0; i < listeRestaurantPhotos.size(); i++){
            LatLng latLng = new LatLng(listeRestaurantPhotos.get(i).getLat(), listeRestaurantPhotos.get(i).getLon());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(listeRestaurantPhotos.get(i).getNomRestaurant()));
        }
    }

    private void initListeRestaurantPhotos(){
        File directory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        listeRestaurantPhotos.clear();
        for(File file: directory.listFiles()){
            ExifInterface exifInterface = null;
            String filePath = file.getAbsolutePath();
            try {
                exifInterface = new ExifInterface(filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            RestaurantPhotos restaurantPhotos = findInListeRestaurantPhotos(exifInterface);
            if(restaurantPhotos == null){
                restaurantPhotos = new RestaurantPhotos();
                restaurantPhotos.setNomRestaurant(exifInterface.getAttribute(ExifInterface.TAG_MAKE));
                restaurantPhotos.setLat(convertToDouble(exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE), "LAT", exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)));
                restaurantPhotos.setLon(convertToDouble(exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE), "LON", exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)));
                restaurantPhotos.getFilePathPhotos().add(filePath);
                listeRestaurantPhotos.add(restaurantPhotos);
            }else {
                restaurantPhotos.getFilePathPhotos().add(filePath);
            }

        }
    }

    private RestaurantPhotos findInListeRestaurantPhotos(ExifInterface exifInterface){
        for(RestaurantPhotos restaurantPhotos : listeRestaurantPhotos){
            if(exifInterface.getAttribute(ExifInterface.TAG_MAKE).equals(restaurantPhotos.getNomRestaurant())){
                return restaurantPhotos;
            }
        }
        return null;
    }

    private double convertToDouble(String coordinateString, String type, String ref) {
        if (coordinateString == null)
            return 0.0;

        String[] parts = coordinateString.split(",");

        double degrees = Double.parseDouble(parts[0].split("/")[0]);
        double minutes = Double.parseDouble(parts[1].split("/")[0]);
        double seconds = Double.parseDouble(parts[2].split("/")[0]);

        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if(type == "LAT"){
            return ref != null && ref.equals("S") ? -result : result;
        }
        return ref != null && ref.equals("W") ? -result : result;
    }

}