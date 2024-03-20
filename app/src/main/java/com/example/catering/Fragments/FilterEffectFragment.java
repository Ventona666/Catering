package com.example.catering.Fragments;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.catering.R;
import com.example.catering.Views.StickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class FilterEffectFragment extends DialogFragment implements SensorEventListener {
    private static final String ARG_IMAGE_URI = "image_uri";

    private static final int STICKER_WIDTH = 100;

    private static final int STICKER_HEIGHT= 100;
    private Uri uri;

    private ImageView imageToFilter;
    private RadioGroup radioGroup;
    private Button buttonCancel;
    private Button buttonApply;
    private SensorManager sensorManager;
    private Sensor sensor;

    private StickerView stickerView;

    private ImageView bisouSticker;
    private ImageView funSticker;
    private ImageView citrouilleSticker;
    private ImageView rireSticker;
    private ImageView diableSticker;

    private Button deleteStickerButton;

    private OnFilterAppliedListener onFilterAppliedListener;

    private float lux;

    public FilterEffectFragment(OnFilterAppliedListener onFilterAppliedListener){
        this.onFilterAppliedListener = onFilterAppliedListener;
    }

    public static FilterEffectFragment newInstance(Uri imageUri, OnFilterAppliedListener onFilterAppliedListener) {
        FilterEffectFragment fragment = new FilterEffectFragment(onFilterAppliedListener);
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_URI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            // Récupérer l'URI de l'image à partir des arguments
            uri = getArguments().getParcelable(ARG_IMAGE_URI);
        }

        View view = inflater.inflate(R.layout.fragment_filtre, container, false);

        imageToFilter = view.findViewById(R.id.imageToFilter);
        radioGroup = view.findViewById(R.id.radioGroup);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonApply = view.findViewById(R.id.buttonApply);

        initStickers(view);

        sensorManager = (SensorManager) getActivity().getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = createUriForTransformedImage();
                sendUriToParentFragment(uri);
                dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                applyFilter();
            }
        });
    }

    private void applyFilter() {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        if (checkedRadioButtonId == R.id.radioButtonBrightness) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        } else if (checkedRadioButtonId == R.id.radioButtonSaturation) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        }
    }

    public static Bitmap applyBrightness(Context context, Uri uri, float brightnessLevel) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);

            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.set(new float[] {
                    1, 0, 0, 0, brightnessLevel,
                    0, 1, 0, 0, brightnessLevel,
                    0, 0, 1, 0, brightnessLevel,
                    0, 0, 0, 1, 0
            });

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

            Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);

            bitmap.recycle();

            return resultBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void applySaturationFilter() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println(event.values[0]);
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){

            Bitmap bitmap = applyBrightness(getContext(), uri, event.values[0]/10);
            imageToFilter.setImageBitmap(bitmap);
        }
        else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY){


            Bitmap bitmap = applyBrightness(getContext(), uri, event.values[0]);
            imageToFilter.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void initStickers(View view){
        stickerView = view.findViewById(R.id.stickerView);
        bisouSticker = view.findViewById(R.id.bisouSticker);
        funSticker = view.findViewById(R.id.funSticker);
        citrouilleSticker = view.findViewById(R.id.citrouilleSticker);
        rireSticker = view.findViewById(R.id.rireSticker);
        diableSticker = view.findViewById(R.id.diableSticker);

        deleteStickerButton = view.findViewById(R.id.deleteButton);
        deleteStickerButton.setOnClickListener(onClickDeleteStickerButton());
        deleteStickerButton.setVisibility(View.GONE);

        bisouSticker.setOnClickListener(onClickSticker(((BitmapDrawable) bisouSticker.getDrawable()).getBitmap()));
        funSticker.setOnClickListener(onClickSticker(((BitmapDrawable) funSticker.getDrawable()).getBitmap()));
        citrouilleSticker.setOnClickListener(onClickSticker(((BitmapDrawable) citrouilleSticker.getDrawable()).getBitmap()));
        rireSticker.setOnClickListener(onClickSticker(((BitmapDrawable) rireSticker.getDrawable()).getBitmap()));
        diableSticker.setOnClickListener(onClickSticker(((BitmapDrawable) diableSticker.getDrawable()).getBitmap()));
    }

    private Bitmap addStickersOnImage(Bitmap originalBitmap) {

        Bitmap combinedBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(combinedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        float scaleFactorX = (float) originalBitmap.getWidth() / imageToFilter.getWidth();
        float scaleFactorY = (float) originalBitmap.getHeight() / imageToFilter.getHeight();

        List<Bitmap> stickersList = stickerView.getStickersList();
        List<PointF> stickerPositions = stickerView.getStickerPositions();
        for (int i = 0; i < stickersList.size(); i++) {
            Bitmap sticker = stickersList.get(i);
            PointF position = stickerPositions.get(i);
            float scaledX = position.x * scaleFactorX;
            float scaledY = position.y * scaleFactorY;
            Bitmap scaledSticker = Bitmap.createScaledBitmap(sticker, (int) (sticker.getWidth() * scaleFactorX), (int) (sticker.getHeight() * scaleFactorX), true);
            canvas.drawBitmap(scaledSticker, scaledX, scaledY, null);
        }

        return combinedBitmap;
    }

    private Uri createUriForTransformedImage(){

        Bitmap imageBitmap = ((BitmapDrawable) imageToFilter.getDrawable()).getBitmap();

        if(!stickerView.getStickersList().isEmpty()){
            imageBitmap = addStickersOnImage(imageBitmap);
        }
        File cacheDir = getContext().getCacheDir();
        File imageFile = new File(cacheDir, "temp_image" + UUID.randomUUID() + ".jpg");
        saveBitmapToFile(imageBitmap, imageFile);

        return Uri.fromFile(imageFile);
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

    private void sendUriToParentFragment(Uri uri) {
        if (onFilterAppliedListener != null) {
            onFilterAppliedListener.onFilterApplied(uri);
        }
    }


    private View.OnClickListener onClickDeleteStickerButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerView.deleteSticker();
                if(stickerView.getStickersList().isEmpty()){
                    deleteStickerButton.setVisibility(View.GONE);
                }
            }
        };
    }

    private View.OnClickListener onClickSticker(Bitmap imageSticker){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stickerView.addSticker(Bitmap.createScaledBitmap(imageSticker, STICKER_WIDTH, STICKER_HEIGHT, true));
                if(stickerView.getStickersList().size() == 1){
                    deleteStickerButton.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    public interface OnFilterAppliedListener {
        void onFilterApplied(Uri uri);
    }
}



