package com.example.catering.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catering.R;

import java.util.List;

public class ListImageAdapter extends RecyclerView.Adapter<ListImageAdapter.ImageViewHolder> {

    private Context context;
    private List<?> listeImagesPath;

    public ListImageAdapter(Context context, List<?> listeImagesPath) {
        this.context = context;
        this.listeImagesPath = listeImagesPath;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_avis_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if(isListOfUris(listeImagesPath)){
            Uri uri = (Uri) listeImagesPath.get(position);
            Glide.with(context)
                    .load(uri)
                    .into(holder.imageView);
        }else {
            String path = (String) listeImagesPath.get(position);
            Glide.with(context)
                    .load(path)
                    .into(holder.imageView);
        }
        
    }

    @Override
    public int getItemCount() {
        return listeImagesPath.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo);
        }
    }

    private boolean isListOfUris(List<?> list) {
        for (Object obj : list) {
            if (!(obj instanceof Uri)) {
                return false;
            }
        }
        return true;
    }
}
