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

import java.util.List;

public class ListAvisAdapter extends ArrayAdapter<Avis> {
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
        updateStars(view, avis.getNote());


        return view;
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