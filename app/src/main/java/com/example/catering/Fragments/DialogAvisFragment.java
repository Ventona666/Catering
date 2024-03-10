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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.catering.Common.DataCallBackImage;
import com.example.catering.Model.Avis;
import com.example.catering.R;
import com.example.catering.Services.FirebaseService;
import com.example.catering.Utils.ListImageAdapter;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DialogAvisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogAvisFragment extends DialogFragment {

    private Avis avis;

    private TextView nomUtilisateur;
    private TextView commentaire;

    private ImageView photo1;

    private ImageView photo2;

    private RecyclerView listeImageAvis;
    private int note;

    private FirebaseService firebaseService = new FirebaseService();

    private List<StorageReference> listeStorageReference = new ArrayList<>();

    public DialogAvisFragment(Avis avis) {
        this.avis = avis;
    }

    public static DialogAvisFragment newInstance(Avis avis) {
        DialogAvisFragment fragment = new DialogAvisFragment(avis);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_avis, null);

        nomUtilisateur = view.findViewById(R.id.nomUtilisateur);
        commentaire = view.findViewById(R.id.commentaire);
        nomUtilisateur.setText(avis.getNomUtilisateur());
        commentaire.setText(avis.getCommentaire());

//        photo1 = view.findViewById(R.id.photo1);
//        photo2 = view.findViewById(R.id.photo2);
        listeImageAvis = view.findViewById(R.id.listImageAvis);
        listeImageAvis.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        initPhotos();

        note = avis.getNote();
        updateStars(view, note);

        builder.setView(view);

        builder.setNegativeButton("Fermer", (dialog, id) -> {
            dialog.dismiss();
        });

        return builder.create();
    }

    private void initPhotos(){
            if(avis.getPhotosUrl() != null){
                firebaseService.getAllUriImageAvis(avis.getPhotosUrl(), new DataCallBackImage<List<Uri>>() {
                    @Override
                    public void onSuccess(List<Uri> data) {
                        Log.d("valeur liste uri", String.valueOf(data.size()));
                        ListImageAdapter listImageAdapter = new ListImageAdapter(getContext(), data);
                        listeImageAvis.setAdapter(listImageAdapter);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Erreur lors de la récupération de l'image", errorMessage);
                    }
                });
            }


//        if(avis.getPhotosUrl() != null){
//            firebaseService.getUriImageAvis(avis.getPhotosUrl().get(0), new DataCallBackImage<Uri>() {
//                @Override
//                public void onSuccess(Uri data) {
//                    Glide.with(getContext())
//                            .load(data)
//                            .into(photo1);
//                }
//
//                @Override
//                public void onError(String errorMessage) {
//                    Log.e("Erreur lors de la récupération de l'image", errorMessage);
//                }
//            });
//
//            if(avis.getPhotosUrl().size() > 1){
//                firebaseService.getUriImageAvis(avis.getPhotosUrl().get(1), new DataCallBackImage<Uri>() {
//                    @Override
//                    public void onSuccess(Uri data) {
//                        Glide.with(getContext())
//                                .load(data)
//                                .into(photo2);
//                    }
//
//                    @Override
//                    public void onError(String errorMessage) {
//                        Log.e("Erreur lors de la récupération de l'image", errorMessage);
//                    }
//                });
//            }else{
//                photo2.setVisibility(View.GONE);
//            }
//
//        }else{
//            photo1.setVisibility(View.GONE);
//            photo2.setVisibility(View.GONE);
//        }

    }

    private void updateStars(View view, int note){
        final ImageView star1 = view.findViewById(R.id.star1);
        final ImageView star2 = view.findViewById(R.id.star2);
        final ImageView star3 = view.findViewById(R.id.star3);
        final ImageView star4 = view.findViewById(R.id.star4);
        final ImageView star5 = view.findViewById(R.id.star5);

        switch (note){
            case 1:
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.empty_star);
                star3.setImageResource(R.drawable.empty_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);
                break;
            case 2:
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.empty_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);
                break;
            case 3:
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.filled_star);
                star4.setImageResource(R.drawable.empty_star);
                star5.setImageResource(R.drawable.empty_star);
                break;
            case 4:
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.filled_star);
                star4.setImageResource(R.drawable.filled_star);
                star5.setImageResource(R.drawable.empty_star);
                break;
            case 5:
                star1.setImageResource(R.drawable.filled_star);
                star2.setImageResource(R.drawable.filled_star);
                star3.setImageResource(R.drawable.filled_star);
                star4.setImageResource(R.drawable.filled_star);
                star5.setImageResource(R.drawable.filled_star);
                break;
        }
    }
}