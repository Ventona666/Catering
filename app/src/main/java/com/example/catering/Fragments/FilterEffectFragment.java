package com.example.catering.Fragments;

import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.catering.R;
import com.mukesh.image_processing.ImageProcessor;

public class FilterEffectFragment extends DialogFragment implements SensorEventListener {
    private static final String ARG_IMAGE_URI = "image_uri";

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
            //applyBrightnessFilter();
        } else if (checkedRadioButtonId == R.id.radioButtonSaturation) {
            applySaturationFilter();
        }
    }

    private void applyBrightnessFilter(float lux) {
        BitmapDrawable drawable = (BitmapDrawable) imageToFilter.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap image = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        ImageProcessor imageProcessor = new ImageProcessor();
        image = imageProcessor.doBrightness(image, 10);
        imageToFilter.setImageBitmap(image);
    }

    private void applySaturationFilter() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT){
            applyBrightnessFilter(event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
