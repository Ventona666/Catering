package com.example.catering.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catering.Common.DataCallBack;
import com.example.catering.Common.DataCallBackImage;
import com.example.catering.Model.Avis;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Services.UtilsService;
import com.example.catering.Utils.ListImageDeleteButtonAdapter;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

    private RecyclerView listeImageDeleteButton;

    private List<Uri> listeUriPhoto = new ArrayList<>();

    private Button envoyerAvisButton;

    private Button appareilPhotoButton;

    private int nbPhotos;

    private ActivityResultLauncher<Intent> appareilPhotoLauncher;

    private ActivityResultLauncher<String> requestPermissionLauncher;

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
        initAppareilPhotoLauncher(view);
        initRequestPermissionLauncher();
        setTextLabelPhotos(view);

        envoyerAvisButton = view.findViewById(R.id.envoyer_avis_button);
        envoyerAvisButton.setEnabled(false);
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
                        displayPhotoButton();
                    }
                }

            }
        });
        listeImageDeleteButton.setAdapter(listImageDeleteButtonAdapter);


        TextWatcher textWatcher = createTextWatcher();
        formElementNomUtilisateur.addTextChangedListener(textWatcher);
        formElementCommentaire.addTextChangedListener(textWatcher);

        envoyerAvisButton.setOnClickListener(onClickEnvoyerAvisButton());
        appareilPhotoButton.setOnClickListener(onClickAppareilPhotoButton());

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

    private void initAppareilPhotoLauncher(View view){
        appareilPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

                        File cacheDir = getContext().getCacheDir();
                        File imageFile = new File(cacheDir, "temp_image" + UUID.randomUUID() + ".jpg");
                        saveBitmapToFile(imageBitmap, imageFile);

                        FilterEffectFragment filterEffectFragment = FilterEffectFragment.newInstance(Uri.fromFile(imageFile), new FilterEffectFragment.OnFilterAppliedListener() {
                            @Override
                            public void onFilterApplied(Uri uri) {
                                ListImageDeleteButtonAdapter adapter = (ListImageDeleteButtonAdapter) listeImageDeleteButton.getAdapter();
                                adapter.add(uri);
                                nbPhotos++;
                                setTextLabelPhotos(view);
                                if(adapter.getListeImagesUri().size() > 1){
                                    maskPhotoButton();
                                }
                            }
                        });
                        filterEffectFragment.show(getChildFragmentManager(), "filter_dialog");

                    }
                });
    }

    private void initRequestPermissionLauncher(){
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        appareilPhotoLauncher.launch(takePictureIntent);
                    } else {
                        Toast.makeText(getContext(), "Permission de la caméra refusée", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveBitmapToFile(Bitmap bitmap, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void requestCameraPermission() {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    //utils methods
    private boolean getFormValidationValue(){
        return !formElementNomUtilisateur.getText().toString().isEmpty() && !formElementCommentaire.getText().toString().isEmpty() && formElementNote != 0;
    }

    private void displayPhotoButton(){
        appareilPhotoButton.setVisibility(View.VISIBLE);
    }

    private void maskPhotoButton(){
        appareilPhotoButton.setVisibility(View.GONE);
    }

    private void setTextLabelPhotos(View view){
        ((TextView) view.findViewById(R.id.labelPhotos)).setText("Photos (" + nbPhotos + ")");
    }

    private String generateRandomPath(){
        return "photo" + UUID.randomUUID() + ".jpg";
    }

    public boolean saveImageInLocalStorage(Drawable imageDrawable, String fileName) {
        String filePath = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + fileName;
        BitmapDrawable drawable = (BitmapDrawable) imageDrawable;
        Bitmap image = drawable.getBitmap();
        OutputStream os = null;
        try {
            os = Files.newOutputStream(Paths.get(filePath));
            image.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();

            //Ajout des informations dans les métadonnées
            ExifInterface exif = new ExifInterface(filePath);
            exif.setAttribute(ExifInterface.TAG_MAKE, removeAccents(this.restaurant.getNom()));

            String latitude = formatCoordinate(this.restaurant.getLat());
            String longitude = formatCoordinate(this.restaurant.getLon());
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, latitude);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, (this.restaurant.getLat() >= 0) ? "N" : "S");
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, longitude);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, (this.restaurant.getLon() >= 0) ? "E" : "W");

            exif.saveAttributes();

            return true;
        } catch (IOException e) {
            Log.e("Erreur stockage", "Erreur lors de la sauvegarde de l'image : " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                Log.e("Erreur fermeture stockage", "Erreur lors de la fermeture du flux de sortie : " + e.getMessage());
            }
        }
    }

    public String formatCoordinate(double coordinate) {
        double absCoordinate = Math.abs(coordinate);
        int degrees = (int) absCoordinate;
        double minutes = (absCoordinate - degrees) * 60;
        int minutesInt = (int) minutes;
        double seconds = (minutes - minutesInt) * 60;
        int secondsInt = (int) seconds;

        String secondsString = String.format("%02d", secondsInt);

        return String.format("%d/1,%d/1,%s/100", degrees, minutesInt, secondsString);
    }

    private String removeAccents(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
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
                    saveImageInLocalStorage(adapter.getImageDrawable(uri), url);
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
                        utilsService.replaceFragment(getParentFragmentManager(), new HomeFragment());
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

    private View.OnClickListener onClickAppareilPhotoButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestCameraPermission();
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    appareilPhotoLauncher.launch(takePictureIntent);
                }
            }
        };
    }



}