package com.example.catering.Services;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Common.DataCallBackImage;
import com.example.catering.Model.Avis;
import com.example.catering.Model.Restaurant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseService {
    private static final String URL_DATABASE = "https://catering-bdd-default-rtdb.europe-west1.firebasedatabase.app";

    private static  final String URL_STORAGE = "gs://catering-bdd.appspot.com";

    private static final String RESTAURANT_REFERENCE = "restaurants";

    private static final String AVIS_REFERENCE = "avis";


    private FirebaseDatabase firebaseDatabase;

    private FirebaseStorage firebaseStorage;


    public FirebaseService(){
        firebaseDatabase = FirebaseDatabase.getInstance(URL_DATABASE);
        firebaseStorage = FirebaseStorage.getInstance(URL_STORAGE);
    }

    public void findAllRestaurants(DataCallBack<List<Restaurant>> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(RESTAURANT_REFERENCE);
        final List<Restaurant> restaurants = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                restaurants.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    restaurants.add(restaurant);
                }

                if (dataCallBack != null) {
                    dataCallBack.onSuccess(restaurants);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataCallBack.onError(error);
            }
        });

    }

    public void findAllAvisByRestaurant(Long restaurantId, DataCallBack<List<Avis>> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(AVIS_REFERENCE);
        Query query = databaseReference.orderByChild("restaurantId").equalTo(restaurantId);
        final List<Avis> listeAvis = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listeAvis.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Avis avis = snapshot.getValue(Avis.class);
                    listeAvis.add(avis);
                }

                if (dataCallBack != null) {
                    dataCallBack.onSuccess(listeAvis);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dataCallBack.onError(error);
            }
        });
    }

    public void createAvis(Avis avis, DataCallBack<String> dataCallBack){
        DatabaseReference databaseReference = firebaseDatabase.getReference(AVIS_REFERENCE);
        String avisKey = databaseReference.push().getKey();
        avis.setId(avisKey);
        databaseReference.child(avisKey).setValue(avis, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if(error != null){
                    dataCallBack.onError(error);

                }else {
                    String messageSuccess = "L'ajout de l'avis a ete correctement effectue en base";
                    dataCallBack.onSuccess(messageSuccess);
                }
            }
        });
    }

    public void saveImage(String imagePath, Drawable imageDrawable, DataCallBackImage<String> dataCallBack){
        BitmapDrawable drawable = (BitmapDrawable) imageDrawable;
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference imagesRef = firebaseStorage.getReference().child(imagePath);

        imagesRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        dataCallBack.onSuccess(imagePath);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        dataCallBack.onError(exception.getMessage());
                    }
                });

    }

    public void getAllUriImageAvis(List<String> urls, final DataCallBackImage<List<Uri>> dataCallBack) {
        final List<Uri> uris = new ArrayList<>();

        for (String url : urls) {
            StorageReference storageRef = firebaseStorage.getReference().child(url);

            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    uris.add(uri);

                    if (uris.size() == urls.size()) {
                        dataCallBack.onSuccess(uris);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    dataCallBack.onError(exception.getMessage());
                }
            });
        }
    }
}
