package com.example.catering.Fragments;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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

import java.io.InputStream;

public class FilterEffectFragment extends DialogFragment implements SensorEventListener {
    private static final String ARG_IMAGE_URI = "image_uri";
    private Uri uri;

    private ImageView imageToFilter;
    private RadioGroup radioGroup;
    private Button buttonCancel;
    private Button buttonApply;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float lux;

    public static FilterEffectFragment newInstance(Uri imageUri) {
        FilterEffectFragment fragment = new FilterEffectFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_IMAGE_URI, imageUri);
        fragment.setArguments(args);
        return fragment;
    }

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
                // Action à effectuer lorsque le bouton Annuler est cliqué
                Toast.makeText(getActivity(), "Annuler", Toast.LENGTH_SHORT).show();
            }
        });

        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action à effectuer lorsque le bouton Appliquer est cliqué
                Toast.makeText(getActivity(), "Appliquer", Toast.LENGTH_SHORT).show();
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
}
