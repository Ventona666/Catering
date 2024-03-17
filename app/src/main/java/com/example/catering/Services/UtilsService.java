package com.example.catering.Services;

import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.catering.Fragments.RestaurantDetailFragment;
import com.example.catering.R;

public class UtilsService {

    public void replaceFragment(FragmentManager fM, Fragment fragment) {
        FragmentManager fragmentManager = fM;
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void updateStars(View view, int note){

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
