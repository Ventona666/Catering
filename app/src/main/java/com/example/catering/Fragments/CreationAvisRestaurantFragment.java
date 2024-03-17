package com.example.catering.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Common.DataCallBackImage;
import com.example.catering.Model.Avis;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.example.catering.Utils.ListImageDeleteButtonAdapter;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreationAvisRestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreationAvisRestaurantFragment extends Fragment {

    private Restaurant restaurant;

    private int formElementNote;

    private EditText formElementNomUtilisateur;

    private EditText formElementCommentaire;

    private ImageView photo1;

    private ImageView photo2;

    private RecyclerView listeImageDeleteButton;

    private List<Uri> listeUriPhoto = new ArrayList<>();

    private Button envoyerAvisButton;

    private Button galerieButton;

    private Button appareilPhotoButton;

    private int nbPhotos;

    private ActivityResultLauncher<String> galerieLauncher;

    private UtilsService utilsService = new UtilsService();

    private FirebaseService firebaseService = new FirebaseService();

    public CreationAvisRestaurantFragment(Restaurant restaurant) {

        this.restaurant = restaurant;
    }

    public static CreationAvisRestaurantFragment newInstance(Restaurant restaurant) {
        CreationAvisRestaurantFragment fragment = new CreationAvisRestaurantFragment(restaurant);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_creation_avis_restaurant, container, false);

        formElementNote = 0;
        nbPhotos = 0 ;
        formElementNomUtilisateur = view.findViewById(R.id.formNomUtilisateur);
        formElementCommentaire = view.findViewById(R.id.formCommentaire);
        initGalerieLauncher(view);
        setTextLabelPhotos(view);

        envoyerAvisButton = view.findViewById(R.id.envoyer_avis_button);
        envoyerAvisButton.setEnabled(false);
        galerieButton = view.findViewById(R.id.galerie_photo_button);
        appareilPhotoButton = view.findViewById(R.id.appareil_photo_button);

        listeImageDeleteButton = view.findViewById(R.id.listImageDeleteButton);
        listeImageDeleteButton.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        ListImageDeleteButtonAdapter listImageDeleteButtonAdapter = new ListImageDeleteButtonAdapter(getContext(), listeUriPhoto, new ListImageDeleteButtonAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(Uri uri) {
                ListImageDeleteButtonAdapter adapter = (ListImageDeleteButtonAdapter) listeImageDeleteButton.getAdapter();
                int position = adapter.getListeImagesUri().indexOf(uri);
                if(position != -1){
                    adapter.deleteItem(position);
                    nbPhotos--;
                    setTextLabelPhotos(view);
                    if(!adapter.getListeImagesUri().isEmpty()){
                        displayPhotoButtons();
                    }
                }

            }
        });
        listeImageDeleteButton.setAdapter(listImageDeleteButtonAdapter);


        TextWatcher textWatcher = createTextWatcher();
        formElementNomUtilisateur.addTextChangedListener(textWatcher);
        formElementCommentaire.addTextChangedListener(textWatcher);

        envoyerAvisButton.setOnClickListener(onClickEnvoyerAvisButton());
        galerieButton.setOnClickListener(onClickGalerieButton());

        //Notes
        final ImageView star1 = view.findViewById(R.id.star1);
        final ImageView star2 = view.findViewById(R.id.star2);
        final ImageView star3 = view.findViewById(R.id.star3);
        final ImageView star4 = view.findViewById(R.id.star4);
        final ImageView star5 = view.findViewById(R.id.star5);

        star1.setOnClickListener(onStarClick(view,1));
        star2.setOnClickListener(onStarClick(view,2));
        star3.setOnClickListener(onStarClick(view,3));
        star4.setOnClickListener(onStarClick(view,4));
        star5.setOnClickListener(onStarClick(view,5));

        return view;
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                envoyerAvisButton.setEnabled(getFormValidationValue());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void initGalerieLauncher(View view){
        galerieLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                      ListImageDeleteButtonAdapter adapter = (ListImageDeleteButtonAdapter) listeImageDeleteButton.getAdapter();
                      adapter.add(uri);
                      nbPhotos++;
                      setTextLabelPhotos(view);
                      if(adapter.getListeImagesUri().size() > 1){
                          maskPhotoButtons();
                      }
                });
    }

    //utils methods
    private boolean getFormValidationValue(){
        return !formElementNomUtilisateur.getText().toString().isEmpty() && !formElementCommentaire.getText().toString().isEmpty() && formElementNote != 0;
    }

    private void displayPhotoButtons(){
        galerieButton.setVisibility(View.VISIBLE);
        appareilPhotoButton.setVisibility(View.VISIBLE);
    }

    private void maskPhotoButtons(){
        galerieButton.setVisibility(View.GONE);
        appareilPhotoButton.setVisibility(View.GONE);
    }

    private void setTextLabelPhotos(View view){
        ((TextView) view.findViewById(R.id.labelPhotos)).setText("Photos (" + nbPhotos + ")");
    }

    private String generateRandomPath(){
        return "images/avis?restaurantId=" + this.restaurant.getId() + "&photoId=" + UUID.randomUUID() +  "/photo.jpg";
    }


    //Listeners
    private View.OnClickListener onClickEnvoyerAvisButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                envoyerAvisButton.setEnabled(false);
                String nomUtilisateur = formElementNomUtilisateur.getText().toString();
                String commentaire = formElementCommentaire.getText().toString();

                Avis avis = new Avis();
                avis.setNomUtilisateur(nomUtilisateur);
                avis.setCommentaire(commentaire);
                avis.setNote(formElementNote);
                avis.setRestaurantId(restaurant.getId());

                ListImageDeleteButtonAdapter adapter = (ListImageDeleteButtonAdapter) listeImageDeleteButton.getAdapter();

                List<String> photosUrl = adapter.getListeImagesUri().stream()
                        .map(uri -> generateRandomPath())
                        .collect(Collectors.toList());
                avis.setPhotosUrl(photosUrl);

                for(int i = 0; i < adapter.getItemCount(); i++){
                    Uri uri = adapter.getListeImagesUri().get(i);
                    String url = photosUrl.get(i);
                    firebaseService.saveImage(url, adapter.getImageDrawable(uri), new DataCallBackImage<String>() {
                        @Override
                        public void onSuccess(String data) {
                            Log.d("Enregistrement image succes", data);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.e("Erreur lors de l'enregistrement de l'image dans firebase", errorMessage);
                        }
                    });
                }

                firebaseService.createAvis(avis, new DataCallBack<String>() {
                    @Override
                    public void onSuccess(String data) {
                        utilsService.replaceFragment(getParentFragmentManager(), new RestaurantDetailFragment(restaurant));
                        Log.d("Creation avis", data);
                    }

                    @Override
                    public void onError(DatabaseError error) {
                        Log.e("Erreur lors de la creation de l'avis", error.toString());
                    }
                });
            }
        };

    }

    private View.OnClickListener onStarClick(View view, int note){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formElementNote = note;
                utilsService.updateStars(view, note);
                envoyerAvisButton.setEnabled(getFormValidationValue());
            }
        };
    }

    private View.OnClickListener onClickGalerieButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galerieLauncher.launch("image/*");
            }
        };
    }

}