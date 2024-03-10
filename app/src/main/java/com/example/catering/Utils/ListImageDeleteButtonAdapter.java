package com.example.catering.Utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.catering.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListImageDeleteButtonAdapter extends RecyclerView.Adapter<ListImageDeleteButtonAdapter.ImageDeleteButtonViewHolder> {

    private Context context;
    private List<Uri> listeImagesUri;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    private Map<Uri, Drawable> drawableMap = new HashMap<>();

    public ListImageDeleteButtonAdapter(Context context, List<Uri> listeImagesUri, OnDeleteButtonClickListener onDeleteButtonClickListener) {
        this.context = context;
        this.listeImagesUri = listeImagesUri;
        this.onDeleteButtonClickListener = onDeleteButtonClickListener;
    }

    @NonNull
    @Override
    public ImageDeleteButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_image_delete_button_item, parent, false);
        return new ImageDeleteButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageDeleteButtonViewHolder holder, int position) {
        Uri uri = listeImagesUri.get(position);
        Log.d("debug ", "je passe");
        holder.imageView.setImageURI(uri);
        drawableMap.put(uri, holder.imageView.getDrawable());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(uri);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listeImagesUri.size();
    }

    public List<Uri> getListeImagesUri() {
        return listeImagesUri;
    }

    public Drawable getImageDrawable(Uri uri){
        return drawableMap.get(uri);
    }

    public void add(Uri uri) {
        Log.d("debug ", "je passe dans le add");
        listeImagesUri.add(uri);
        notifyDataSetChanged();
        Log.d("debug ", "je passe dans le add : " + listeImagesUri.get(listeImagesUri.size()-1));
    }

    public void deleteItem(int position) {
        listeImagesUri.remove(position);
        notifyItemRemoved(position);
    }

    public static class ImageDeleteButtonViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button deleteButton;

        public ImageDeleteButtonViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.photo);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(Uri uri);
    }
}
