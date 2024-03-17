package com.example.catering.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.catering.Model.Avis;
import com.example.catering.Model.Restaurant;
import com.example.catering.R;
import com.example.catering.Services.UtilsService;

import java.util.List;

public class ListAvisAdapter extends ArrayAdapter<Avis> {

    private UtilsService utilsService = new UtilsService();
    public ListAvisAdapter(@NonNull Context context, List<Avis> avis) {
        super(context, R.layout.list_item, avis);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Avis avis = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_avis_item, parent, false);
        }

        TextView nomUtilisateur = view.findViewById(R.id.nomUtilisateur);
        TextView commentaire = view.findViewById(R.id.commentaire);
        nomUtilisateur.setText(avis.getNomUtilisateur());
        commentaire.setText(avis.getCommentaire());
        utilsService.updateStars(view, avis.getNote());


        return view;
    }
}